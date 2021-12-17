package no.kristiania.booking

import no.kristiania.booking.db.Booking
import no.kristiania.booking.dto.BookingDto

// Code inspired from class
object DtoConverter {

    fun transform(booking: Booking) : BookingDto {
        return BookingDto().apply{
            id = booking.id
            userId = booking.user.userId
            tripId = booking.trip.id
        }
    }
}