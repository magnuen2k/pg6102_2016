package no.kristiania.trips.service

import no.kristiania.trips.db.Trip
import no.kristiania.trips.dto.TripDto
import no.kristiania.trips.repository.BoatRepository
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TripService (
    private val tripRepository: TripRepository,
    private val boatRepository: BoatRepository,
    private val portRepository: PortRepository
    ) {

    fun getTrips(): MutableIterable<Trip> {
        return tripRepository.findAll()
    }

    // Add trip
    fun addTrip(trip: TripDto): Boolean {

        // TODO Check if boat and ports exists better

        if (trip.boat == null || trip.destination == null || trip.origin == null) {
            return false
            //throw IllegalStateException("Not provided proper data")
        }

        val boat = boatRepository.findByIdOrNull(trip.boat!!)
        val destination = portRepository.findByIdOrNull(trip.destination!!)
        val origin =  portRepository.findByIdOrNull(trip.origin!!)

        val t = Trip()
        t.origin = origin
        t.destination = destination
        t.boat = boat
        t.crew = trip.crew

        tripRepository.save(t)

        return true

    }

}