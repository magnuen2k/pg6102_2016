package no.kristiania.trips

import no.kristiania.trips.db.Trip
import no.kristiania.trips.dto.TripDto

object DtoConverter {
    fun transform(trip: Trip) : TripDto = trip.run {
        TripDto(tripId, origin, destination, boat, crew, passengers, tripYear)
    }

    fun transform(trips: Iterable<Trip>) : List<TripDto> = trips.map { transform(it) }
}