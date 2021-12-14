package no.kristiania.trips

import io.swagger.annotations.ApiOperation
import no.kristiania.restdto.RestResponseFactory
import no.kristiania.restdto.WrappedResponse
import no.kristiania.trips.db.Trip
import no.kristiania.trips.dto.TripDto
import no.kristiania.trips.service.TripService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trips")
class RestAPI (private val tripService: TripService) {

    @ApiOperation("GET trips")
    @GetMapping
    fun getAll(): MutableIterable<Trip> {
        return tripService.getTrips()
    }

    @ApiOperation("POST a new trip")
    @PostMapping
    fun addTrip(@RequestBody tripDto: TripDto): ResponseEntity<WrappedResponse<Void>> {
        val response = tripService.addTrip(tripDto)
        if(!response) {
            return RestResponseFactory.userFailure("Wrong data provided", 400)
        }
        return RestResponseFactory.noPayload(201)
    }
}