package no.kristiania.trips.service

import no.kristiania.trips.DtoConverter
import no.kristiania.trips.db.Trip
import no.kristiania.trips.dto.PatchTripDto
import no.kristiania.trips.dto.TripDto
import no.kristiania.trips.repository.BoatRepository
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Service
@Transactional
class TripService (
    private val tripRepository: TripRepository,
    private val boatRepository: BoatRepository,
    private val portRepository: PortRepository,
    private val em: EntityManager
    ) {

    // Implement pagination
    fun getTrips(): MutableIterable<Trip> {
        return tripRepository.findAll()
    }

    fun getNextPage(size: Int, keysetId: Long? = null, keysetYear: Int? = null): List<TripDto> {
        if (size < 1 || size > 1000) {
            throw IllegalArgumentException("Invalid size value: $size")
        }

        if((keysetId==null && keysetYear!=null) || (keysetId!=null && keysetYear==null)){
            throw IllegalArgumentException("keysetId and keysetYear should be both missing, or both present")
        }

        val query: TypedQuery<Trip>
        if(keysetId == null) {
            query = em.createQuery(
                "select t from Trip t order by t.tripYear DESC, t.tripId DESC",
                Trip::class.java
            )
        } else {
            query = em.createQuery(
                "select t from Trip t where t.tripYear<?2 or (t.tripYear=?2 and t.tripId<?1) order by t.tripYear DESC, t.tripId DESC",
                Trip::class.java
            )
            query.setParameter(1, keysetId)
            query.setParameter(2, keysetYear)
        }
        query.maxResults = size

        val results = DtoConverter.transform(query.resultList)

        return results
    }

    // Add trip
    fun addTrip(trip: TripDto): Trip {

        // TODO Check if boat and ports exists better

        if (trip.boat == null || trip.destination == null || trip.origin == null) {
            //return false
            throw IllegalStateException("Not provided proper data")
        }

        /*val boat = boatRepository.findByIdOrNull(trip.boat!!.name!!)
        val destination = portRepository.findByIdOrNull(trip.destination!!.name!!)
        val origin =  portRepository.findByIdOrNull(trip.origin!!.name!!)*/

        // TODO Check if boat has space

        val t = Trip()
        t.origin = trip.origin
        t.destination = trip.destination
        t.boat = trip.boat
        t.crew = trip.crew
        t.passengers = trip.passengers
        t.tripYear = trip.tripYear

        return tripRepository.save(t)

    }

    fun getTripById(id: Long): TripDto? {
        val trip = tripRepository.findByIdOrNull(id)

        if(trip != null) {
            return DtoConverter.transform(trip)
        }

        return null
    }

    fun getTripsByIds(tripIds: MutableList<Long>): List<TripDto>? {
        if(tripIds.size <= 0) {
            return null
        }

        val trips: MutableList<Trip> = mutableListOf()

        for (id in tripIds) {
            trips.add(tripRepository.findByIdOrNull(id)!!)
        }

        return DtoConverter.transform(trips)

    }

    fun updateTrip(trip: PatchTripDto, id: Long): Boolean {
        if(trip.crew == 0 || trip.crew == 0) {
            return false
        }

        val t = tripRepository.findByIdOrNull(id) ?: return false

        t.crew = trip.crew
        t.passengers = trip.passengers

        return true
    }

    fun deleteTrip(id: Long) : Boolean {
        return if (tripRepository.existsById(id)) {
            tripRepository.deleteById(id)
            // TODO Notify about trip deletion to AMQP
            true
        } else {
            false
        }
    }
}