package no.kristiania.booking

import io.restassured.RestAssured
import net.minidev.json.JSONObject
import no.kristiania.booking.db.Trip
import no.kristiania.booking.repository.BookingRepository
import no.kristiania.booking.repository.TripRepository
import no.kristiania.booking.repository.UserRepository
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

    }
}