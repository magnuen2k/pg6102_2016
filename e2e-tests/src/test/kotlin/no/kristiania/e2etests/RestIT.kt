package no.kristiania.e2etests

import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.kristiania.e2etests.dto.BoatDto
import no.kristiania.e2etests.dto.BookingDto
import no.kristiania.e2etests.dto.PortDto
import no.kristiania.e2etests.dto.TripDto

import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

@Disabled
@Testcontainers
class RestIT {

    companion object {

        init {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
            RestAssured.port = 80
        }

        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        @Container
        @JvmField
        val env = KDockerComposeContainer("pg6100_2016", File("../docker-compose.yml"))
            .withExposedService("discovery", 8500,
                Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(240)))
            .withLogConsumer("booking_1") { print("[BOOKING_0] " + it.utf8String) }
            .withLogConsumer("booking_2") { print("[BOOKING_1] " + it.utf8String) }
            .withLogConsumer("trips") { print("[TRIPS] " + it.utf8String) }
            .withLocalCompose(true)


        @BeforeAll
        @JvmStatic
        fun waitForServers() {

            Awaitility.await().atMost(240, TimeUnit.SECONDS)
                .pollDelay(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(10))
                .ignoreExceptions()
                .until {

                    RestAssured.given().baseUri("http://${env.getServiceHost("discovery", 8500)}")
                        .port(env.getServicePort("discovery", 8500))
                        .get("/v1/agent/services")
                        .then()
                        .body("size()", CoreMatchers.equalTo(4))

                    true
                }
        }
    }

    @Test
    fun testCreateUser() {
        Awaitility.await().atMost(120, TimeUnit.SECONDS)
            .pollInterval(Duration.ofSeconds(10))
            .ignoreExceptions()
            .until {

                val id = "foo_testCreateUser_" + System.currentTimeMillis()

                RestAssured.given().get("/api/booking/mybookings")
                    .then()
                    .statusCode(401)


                val password = "123456"

                val cookie = RestAssured.given().contentType(ContentType.JSON)
                    .body("""
                                {
                                    "userId": "$id",
                                    "password": "$password"
                                }
                            """.trimIndent())
                    .post("/api/auth/signUp")
                    .then()
                    .statusCode(201)
                    .header("Set-Cookie", CoreMatchers.not(CoreMatchers.equalTo(null)))
                    .extract().cookie("SESSION")

                RestAssured.given().cookie("SESSION", cookie)
                    .get("/api/auth/user")
                    .then()
                    .statusCode(200)

                true
            }
    }

    @Test
    fun testGetTrips() {
        Awaitility.await().atMost(120, TimeUnit.SECONDS)
            .pollInterval(Duration.ofSeconds(10))
            .ignoreExceptions()
            .until {
                RestAssured.given().get("/api/trips")
                    .then()
                    .statusCode(200)
                    .body("data.list.size()", Matchers.equalTo(4))
                true
            }
    }

    @Test
    fun testCreateBookingAccessControl() {
        // Log in
        val user = "test-" + System.currentTimeMillis()

        RestAssured.given().post("/api/booking").then().statusCode(401)
        RestAssured.given().get("/api/booking/1").then().statusCode(401)
        RestAssured.given().get("/api/booking/mybookings").then().statusCode(401)

        val cookie = RestAssured.given().contentType(ContentType.JSON)
            .body("""
                                {
                                    "userId": "$user",
                                    "password": "123456"
                                }
                            """.trimIndent())
            .post("/api/auth/signUp")
            .then()
            .statusCode(201)
            .header("Set-Cookie", CoreMatchers.not(CoreMatchers.equalTo(null)))
            .extract().cookie("SESSION")

        // There is no data provided, and no trip to book, but getting a 400 here will show that we got to the endpoint
        RestAssured.given().cookie("SESSION", cookie)
            .header("Content-Type", "application/json")
            .post("/api/booking")
            .then()
            .statusCode(400)
    }

    // AMQP (HAS TO BE DEFAULT BOAT AND PORT TO WORK)!!
    @Test
    fun testCreateTrip() {
        // Create boat, ports and trip
        val b = BoatDto("Test", 32, "Test2", 6, 20)
        val p1 = PortDto("port1", 50)
        val p2 = PortDto("port2", 100)

        val t = TripDto()
        t.boat = b
        t.origin = p1
        t.destination = p2
        t.tripYear = 2021

        // Log in
        val user = "test-" + System.currentTimeMillis()

        val cookie = RestAssured.given().contentType(ContentType.JSON)
            .body("""
                                {
                                    "userId": "$user",
                                    "password": "123456"
                                }
                            """.trimIndent())
            .post("/api/auth/signUp")
            .then()
            .statusCode(201)
            .header("Set-Cookie", CoreMatchers.not(CoreMatchers.equalTo(null)))
            .extract().cookie("SESSION")

        // Register trip through API, this service sends AMQP message to booking service about trip creation
        val response = RestAssured.given().cookie("SESSION", cookie)
            .header("Content-Type", "application/json")
            .body(t)
            .put("/api/trips/")


        // Must be the id of the first "user" added trip, optimized should be reading from response
        val tripId = 11L

        // Book the trip and get booking to see tripId
        RestAssured.given().cookie("SESSION", cookie)
            .header("Content-Type", "application/json")
            .body(BookingDto(userId = user, tripId = tripId))
            .post("/api/booking/")
            .then()
            .statusCode(201)

        RestAssured.given().cookie("SESSION", cookie)
            .header("Content-Type", "application/json")
            .get("/api/booking/1")
            .then()
            .statusCode(200)
            .body("data.tripId", Matchers.equalTo(11))
    }

}