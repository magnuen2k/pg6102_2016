package no.kristiania.trips

import io.swagger.annotations.ApiOperation
import no.kristiania.restdto.PageDto
import no.kristiania.restdto.RestResponseFactory
import no.kristiania.restdto.WrappedResponse
import no.kristiania.trips.db.Boat
import no.kristiania.trips.db.Port
import no.kristiania.trips.db.Trip
import no.kristiania.trips.dto.TripDto
import no.kristiania.trips.service.BoatService
import no.kristiania.trips.service.PortService
import no.kristiania.trips.service.TripService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/trips")
class RestAPI (
    private val tripService: TripService,
    private val boatService: BoatService,
    private val portService: PortService,
    private val rabbit: RabbitTemplate,
    private val fanout: FanoutExchange
    ) {

    companion object{
        private val log = LoggerFactory.getLogger(RestAPI::class.java)
    }

   /* @ApiOperation("GET trips")
    @GetMapping
    fun getAll(): MutableIterable<Trip> {
        return tripService.getTrips()
    }*/

    @ApiOperation("Retrieve all trips")
    @GetMapping
    fun getTrips(
        @RequestParam("keysetId", required = false) keysetId: Long?,
        @RequestParam("keysetYear", required = false) keysetYear: Int?
    ): ResponseEntity<WrappedResponse<PageDto<TripDto>>> {

        val page = PageDto<TripDto>()

        val n = 4
        val trips = tripService.getNextPage(n, keysetId, keysetYear)
        page.list = trips

        if(trips.size == n) {
            val last = trips.last()
            page.next = "/api/trips?keysetId=${last.tripId}&keysetYear=${last.tripYear}"
        }

        return ResponseEntity
            .status(200)
            .cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic())
            .body(WrappedResponse(200, page))

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

    @ApiOperation("GET All boats")
    @GetMapping("/boats")
    fun getBoats(): ResponseEntity<WrappedResponse<MutableIterable<Boat>>> {
        return RestResponseFactory.payload(200, boatService.getBoats())
    }

    @ApiOperation("GET All Ports")
    @GetMapping("/ports")
    fun getPorts(): ResponseEntity<WrappedResponse<MutableIterable<Port>>> {
        return RestResponseFactory.payload(200, portService.getPorts())
    }
}