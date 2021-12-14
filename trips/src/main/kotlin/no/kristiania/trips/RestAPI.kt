package no.kristiania.trips

import io.swagger.annotations.ApiOperation
import no.kristiania.restdto.RestResponseFactory
import no.kristiania.restdto.WrappedResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trips")
class RestAPI {

    @ApiOperation("Test endpoint")
    @GetMapping
    fun getEndpointTest(): ResponseEntity<WrappedResponse<String>> {
        return RestResponseFactory.payload(200, "Endpoint Reached")
    }
}