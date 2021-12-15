package no.kristiania.trips.service

import no.kristiania.trips.db.Boat
import no.kristiania.trips.repository.BoatRepository
import org.springframework.stereotype.Service

@Service
class BoatService(
    private val boatRepository: BoatRepository
) {

    fun getBoats(): MutableIterable<Boat> {
        return boatRepository.findAll();
    }
}