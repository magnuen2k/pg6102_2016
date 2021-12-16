package no.kristiania.weather

import io.swagger.annotations.ApiOperation
import no.kristiania.restdto.RestResponseFactory
import no.kristiania.restdto.WrappedResponse
import no.kristiania.weather.service.WeatherService
import no.kristiania.weather.service.WeatherStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/weather")
class RestAPI(
    private val weatherService: WeatherService
) {

    @ApiOperation("Get the weather status for a given port")
    @GetMapping("/{id}")
    fun getWeather(@PathVariable id: String): ResponseEntity<WrappedResponse<String>> {
        val status = weatherService.getWeatherStatus(id)

         return RestResponseFactory.payload(200, status)

    }
}