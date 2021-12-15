package no.kristiania.booking

import io.swagger.annotations.ApiOperation
import no.kristiania.booking.dto.BookingDto
import no.kristiania.booking.service.BookingService
import no.kristiania.restdto.RestResponseFactory
import no.kristiania.restdto.WrappedResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/booking")
class RestAPI(
    private val bookingService: BookingService
) {

    // Booking a trip will create a trip. When creation of trip happens in trip api, will send AMQP to booking api and initializes

    @ApiOperation("Logged in user can start the planned trip")
    @PostMapping
    fun startTrip(@RequestBody bookingDto: BookingDto): ResponseEntity<WrappedResponse<Void>> {
        val booking = bookingService.startTrip(bookingDto)

        return RestResponseFactory.created(URI.create("/api/booking/${booking.id}"))
    }

    // Should know whos logged in
    @ApiOperation("GET a specific booking")
    @GetMapping("/{id}")
    fun getBooking(@PathVariable id: Long): ResponseEntity<WrappedResponse<BookingDto?>> {

        val booking = bookingService.getBookingById(id) ?: return RestResponseFactory.userFailure("Booking does not exist")

        return RestResponseFactory.payload(200, booking)
    }

    // Get trips from user
}