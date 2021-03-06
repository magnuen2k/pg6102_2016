package no.kristiania.trips

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.minidev.json.JSONObject
import no.kristiania.restdto.WrappedResponse
import no.kristiania.trips.db.Boat
import no.kristiania.trips.db.Port
import no.kristiania.trips.db.Trip
import no.kristiania.trips.dto.PatchTripDto
import no.kristiania.trips.repository.BoatRepository
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import wiremock.com.fasterxml.jackson.databind.ObjectMapper
import javax.annotation.PostConstruct

@ActiveProfiles("test")
@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [(RestAPITest.Companion.Initializer::class)])
class RestAPITest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var boatRepository: BoatRepository

    @Autowired
    private lateinit var portRepository: PortRepository

    companion object {

        private lateinit var wiremockServer: WireMockServer

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServer = WireMockServer(
                WireMockConfiguration.wireMockConfig().dynamicPort().notifier(
                ConsoleNotifier(true)
            ))
            wiremockServer.start()


            val dto = WrappedResponse(code = 200, data = FakeData.getWeatherStatus()).validated()
            val json = ObjectMapper().writeValueAsString(dto)

            wiremockServer.stubFor(
                WireMock.get(WireMock.urlMatching("/api/weather/*"))
                    .willReturn(
                        WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(json)))
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @Container
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

                TestPropertyValues
                    .of("spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                        "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672),
                        "weatherServiceAddress: localhost:${wiremockServer.port()}")
                    .applyTo(configurableApplicationContext.environment);
            }
        }
    }

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun testAccessControl() {
        RestAssured.given().get("/api/trips/1").then().statusCode(401)
        RestAssured.given().get("/api/trips/byIds").then().statusCode(401)
        RestAssured.given().put("/api/trips/").then().statusCode(401)

        // Try to get something that does not exist is saver than depending on some data being there
        RestAssured.given().auth().basic("bar", "123")
            .get("/api/trips/" + System.currentTimeMillis())
            .then()
            .statusCode(400)
    }

    // Should be same as in application
    val page: Int = 4

    @Test
    fun testGetPageOfTrips() {
        RestAssured.given().accept(ContentType.JSON)
            .get("api/trips")
            .then()
            .statusCode(200)
            .body("data.list.size()", CoreMatchers.equalTo(page))
    }

    @Test
    fun testGetTwoPages() {
        val response = RestAssured.given().accept(ContentType.JSON)
            .get("api/trips")

        val data: HashMap<String, String> = response.body.jsonPath().getJsonObject("data")
        val next: String = data["next"].toString()

        RestAssured.given()
            .get(next)
            .then()
            .statusCode(200)
            .body("data.list.size()", CoreMatchers.equalTo(page))
    }

    @Test
    fun testPutNewTrip() {
        val body = JSONObject()
        body["origin"] = createPort(20)
        body["destination"] = createPort(90)
        body["boat"] = createBoat("foo", 4, 34)
        body["crew"] = 3
        body["passengers"] = 10
        body["tripYear"] = 2021

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .body(body)
            .put("/api/trips/")
            .then()
            .statusCode(201)

    }

    @Test
    fun testDeleteTrip() {
        val t = getTripWithRandomBoatAndPort()

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .delete("api/trips/${t.tripId}")
            .then()
            .statusCode(204)
    }

    @Test
    fun testGetTripById() {
        val t = getTripWithRandomBoatAndPort()

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .get("/api/trips/${t.tripId}")
            .then()
            .statusCode(200)
    }

    @Test
    fun testGetAllBoats() {
        val b = createBoat("foo", 2, 3)

        RestAssured.given()
            .header("Content-Type", "application/json")
            .get("/api/trips/boats")
            .then()
            .statusCode(200)
    }

    @Test
    fun testGetAllPorts() {
        val b = createPort(30)

        RestAssured.given()
            .header("Content-Type", "application/json")
            .get("/api/trips/ports")
            .then()
            .statusCode(200)
    }

    // This endpoint is getting hit from booking api where the trip ids are filtered to a users booked trips
    @Test
    fun testGetTripsByTripIds() {
        val t1 = getTripWithRandomBoatAndPort()
        val t2 = getTripWithRandomBoatAndPort()
        val t3 = getTripWithRandomBoatAndPort()

        val tripIds: MutableList<Long> = mutableListOf()
        tripIds.add(t1.tripId!!)
        tripIds.add(t2.tripId!!)
        tripIds.add(t3.tripId!!)

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .body(tripIds)
            .post("/api/trips/byIds")
            .then()
            .statusCode(200)
    }

    @Test
    fun testPatchTrip() {
        val t = getTripWithRandomBoatAndPort()

        val patch = PatchTripDto(15, 2)

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .body(patch)
            .patch("/api/trips/${t.tripId}")
            .then()
            .statusCode(204)
    }

    fun getTripWithRandomBoatAndPort() : Trip {
        val b = createBoat("test", 15, 21)
        val p1 = createPort(200)
        val p2 = createPort(140)
        return createTrip(2020, 20, 5, b, p2, p1)
    }

    fun createPort(maxBoats: Int) : Port {
        val p = Port()
        p.maxBoats = maxBoats
        p.name = "port-" + System.currentTimeMillis()
        return portRepository.save(p)
    }

    fun createBoat(builder: String, crewSize: Int, length: Int) : Boat {
        val b = Boat()
        b.builder = builder
        b.crewSize = crewSize
        b.length = length
        b.name = "boat-" + System.currentTimeMillis()
        return boatRepository.save(b)
    }

    fun createTrip(year: Int, passengers: Int, crew: Int, boat: Boat, origin: Port, destination: Port): Trip {
        val t = Trip()
        t.tripYear = year
        t.passengers = passengers
        t.status = "Warning"
        t.crew = crew
        t.boat = boat
        t.destination = destination
        t.origin = origin
        return tripRepository.save(t)
    }
}