package no.kristiania.trips

import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.minidev.json.JSONObject
import no.kristiania.trips.db.Boat
import no.kristiania.trips.db.Port
import no.kristiania.trips.db.Status
import no.kristiania.trips.db.Trip
import no.kristiania.trips.repository.BoatRepository
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.hamcrest.CoreMatchers
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

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @Container
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

                TestPropertyValues
                    .of("spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                        "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672))
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

        // TODO Could put new boat and ports to repos here for safety

        val body = JSONObject()
        body["origin"] = "port2"
        body["destination"] = "port1"
        body["boat"] = "test"
        body["crew"] = 3
        body["passengers"] = 10

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(body)
            .put("api/trips/")
            .then()
            .statusCode(201)

    }

    @Test
    fun testDeleteTrip() {

        // PUT IN DIFFERENT FUNCTION
        val b = createBoat("test", 15, 21, "testy")
        val p1 = createPort(200, "port1")
        val p2 = createPort(140, "port2")
        val t = createTrip(2020, 20, 5, b, p2, p1)

        RestAssured.given()
            .header("Content-Type", "application/json")
            .delete("api/trips/${t.tripId}")
            .then()
            .statusCode(204)
    }

    fun createPort(maxBoats: Int, name: String) : Port {
        val p = Port()
        p.maxBoats = maxBoats
        p.name = name
        return portRepository.save(p)
    }

    fun createBoat(builder: String, crewSize: Int, length: Int, name: String) : Boat {
        val b = Boat()
        b.builder = builder
        b.crewSize = crewSize
        b.length = length
        b.name = name
        return boatRepository.save(b)
    }

    fun createTrip(year: Int, passengers: Int, crew: Int, boat: Boat, origin: Port, destination: Port): Trip {
        val t = Trip()
        t.tripYear = year
        t.passengers = passengers
        t.status = Status.WARNING
        t.crew = crew
        t.boat = boat
        t.destination = destination
        t.origin = origin
        return tripRepository.save(t)
    }
}