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

    @ApiOperation("POST a new planned trip")
    @PostMapping
    fun addTrip(@RequestBody tripDto: TripDto): ResponseEntity<WrappedResponse<Void>> {
        val response = try {
            tripService.addTrip(tripDto)
        } catch (e: IllegalStateException) {
            return RestResponseFactory.userFailure("Wrong data provided", 400)
        }

        log.info("SHOULD BE 11: ${response.tripId}")

        rabbit.convertAndSend(fanout.name, "", response.tripId!!)

        return RestResponseFactory.noPayload(201)
    }
}