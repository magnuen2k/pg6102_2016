package no.kristiania.trips

import io.swagger.annotations.ApiOperation
import no.kristiania.restdto.RestResponseFactory
import no.kristiania.restdto.WrappedResponse
import no.kristiania.trips.db.Trip
import no.kristiania.trips.dto.TripDto
import no.kristiania.trips.service.TripService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trips")
class RestAPI (
    private val tripService: TripService,
    private val rabbit: RabbitTemplate,
    private val fanout: FanoutExchange
    ) {

    companion object{
        private val log = LoggerFactory.getLogger(RestAPI::class.java)
    }

    @ApiOperation("GET trips")
    @GetMapping
    fun getAll(): MutableIterable<Trip> {
        return tripService.getTrips()
    }

    @ApiOperation("PUT a new planned trip")
    @PutMapping
    fun addTrip(@RequestBody tripDto: TripDto): ResponseEntity<WrappedResponse<Void>> {
        val response = try {
            tripService.addTrip(tripDto)
        } catch (e: IllegalStateException) {
            return RestResponseFactory.userFailure("Wrong data provided", 400)
        }

        rabbit.convertAndSend(fanout.name, "", response.tripId!!)

        return RestResponseFactory.noPayload(201)
    }

    @ApiOperation("DELETE a trip by id")
    @DeleteMapping("/{id}")
    fun deleteTrip(@PathVariable("id") id: Long): ResponseEntity<WrappedResponse<Void>> {
        return if (tripService.deleteTrip(id)) {
            RestResponseFactory.noPayload(204)
        } else {
            RestResponseFactory.userFailure("Trip with id: $id was not deleted")
        }
    }
}