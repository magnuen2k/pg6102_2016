package no.kristiania.booking

import wiremock.com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import net.minidev.json.JSONObject
import no.kristiania.booking.db.Booking
import no.kristiania.booking.db.Trip
import no.kristiania.booking.db.User
import no.kristiania.booking.repository.BookingRepository
import no.kristiania.booking.repository.TripRepository
import no.kristiania.booking.repository.UserRepository
import no.kristiania.restdto.WrappedResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
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
    private lateinit var bookingRepository: BookingRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    companion object {

        private lateinit var wiremockServer: WireMockServer

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort().notifier(
                ConsoleNotifier(true)
            ))
            wiremockServer.start()


            val dto = WrappedResponse(code = 200, data = FakeData.getTripDtos()).validated()
            val json = ObjectMapper().writeValueAsString(dto)

            wiremockServer.stubFor(
                WireMock.get(WireMock.urlMatching("/api/trips/byIds"))
                    .willReturn(WireMock.aResponse()
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
                        "tripServiceAddress: localhost:${wiremockServer.port()}")
                    .applyTo(configurableApplicationContext.environment);
            }
        }
    }

    /*@BeforeEach
    fun initTest() {
        tripRepository.deleteAll()
    }*/

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun testAccessControl() {
        RestAssured.given().get("/api/booking/1").then().statusCode(401)
        RestAssured.given().get("/api/booking/mybookings").then().statusCode(401)
        RestAssured.given().post("/api/booking/").then().statusCode(401)

        RestAssured.given().auth().basic("bar", "123")
            .get("/api/booking/1")
            .then()
            .statusCode(400)
    }

    @Test
    fun testStartTrip() {
        var t = Trip()
        t.id = 1
        t = tripRepository.save(t)

        val body = JSONObject()
        body["userId"] = "foo"
        body["tripId"] = t.id

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .body(body)
            .post("/api/booking")
            .then()
            .statusCode(201)
    }

    @Test
    fun testGetBooking() {
        val t = Trip()
        t.id = System.currentTimeMillis()
        tripRepository.save(t)

        val user = User()
        user.userId = "foo" + System.currentTimeMillis()
        userRepository.save(user)

        var b = Booking(user = user, trip = t)
        b = bookingRepository.save(b)

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .get("/api/booking/${b.id}")
            .then()
            .statusCode(200)
    }

    // Hitting endpoint that should be mocked by wiremock
    /*@Test
    fun testGetTripsForUser() {
        val t = Trip()
        t.id = System.currentTimeMillis()
        tripRepository.save(t)

        val user = User()
        user.userId = "foo"
        userRepository.save(user)

        var b = Booking(user = user, trip = t)
        b = bookingRepository.save(b)

        RestAssured.given().auth().basic("foo", "123")
            .header("Content-Type", "application/json")
            .get("/api/booking/mybookings")
            .then()
            .statusCode(200)
    }*/
}