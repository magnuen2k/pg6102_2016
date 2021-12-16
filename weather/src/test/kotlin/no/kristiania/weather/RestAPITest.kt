package no.kristiania.weather

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct


@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestAPITest {

    @LocalServerPort
    protected var port = 0

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun testGetWeather() {
        RestAssured.given()
            .get("/api/weather/someport")
            .then()
            .statusCode(200)

        RestAssured.given()
            .get("/api/weather/someport")
            .then()
            .statusCode(200)

        RestAssured.given()
            .get("/api/weather/someport")
            .then()
            .statusCode(200)

        RestAssured.given()
            .get("/api/weather/someport")
            .then()
            .statusCode(200)
    }

}