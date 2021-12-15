package no.kristiania.booking.service

import no.kristiania.booking.db.Trip
import no.kristiania.booking.repository.TripRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TripService (
    private val tripRepository: TripRepository
    ) {

    fun getTrip(tripId: Long) : Trip? {
        return tripRepository.findWithLock(tripId)
    }
}