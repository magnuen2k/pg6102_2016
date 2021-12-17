package no.kristiania.booking

import no.kristiania.booking.dto.BoatDto
import no.kristiania.booking.dto.PortDto
import no.kristiania.booking.dto.TripDto

// Copied from class, but modified to fit
object FakeData {
    fun getTripDtos() : List<TripDto> {

        val trips: MutableList<TripDto> = mutableListOf()

        val b = BoatDto("test", 2, "test", 3, 10)
        val p1 = PortDto("port1", 2)
        val p2 = PortDto("port2", 3)

        val t = TripDto(System.currentTimeMillis(), p1, p2, b, 2, 3, 2001)

        trips.add(t)

        return trips

    }
}