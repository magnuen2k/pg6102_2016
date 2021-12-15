package no.kristiania.booking.service

import no.kristiania.booking.MOMListener
import no.kristiania.booking.db.Booking
import no.kristiania.booking.db.Status
import no.kristiania.booking.dto.BookingDto
import no.kristiania.booking.repository.BookingRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val userService: UserService,
    private val tripService: TripService
) {

    companion object {
        private val log = LoggerFactory.getLogger(BookingService::class.java)
    }

    fun startTrip(booking: BookingDto) : Booking {
        // SHOULD CHECK IF USER AND TRIP EXISTS FIRST

        val trip = tripService.getTrip(booking.tripId!!)
        val user = userService.getUserId(booking.userId!!)

        log.info("Trip: ${trip?.id}")
        log.info("User: ${user?.userId}")

        val b = Booking(user = user!!, trip = trip!!)
        b.ongoing = Status.ONGOING

        bookingRepository.save(b)

        // Broadcast to trip api that trip has started

        return b
    }

}