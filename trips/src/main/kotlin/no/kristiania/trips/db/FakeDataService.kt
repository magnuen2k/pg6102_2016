package no.kristiania.trips.db

import no.kristiania.trips.repository.BoatRepository
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Service
@Transactional
class FakeDataService (
    val tripRepository: TripRepository,
    val boatRepository: BoatRepository,
    val portRepository: PortRepository
    ) {

    @PostConstruct
    fun init() {

        // Should add a lot of boats and ports to choose from (Should be done from sql later)

        val b = Boat("Test", 32, "Test2", 6, 20)
        boatRepository.save(b)

        val p1 = Port("port1", 50)
        val p2 = Port("port2", 100)
        portRepository.save(p1)
        portRepository.save(p2)

        // To test pagination
        createTrip(b, 2, p2, p1, Status.WARNING)
        createTrip(b, 4, p2, p1, Status.WARNING)
        createTrip(b, 5, p2, p1, Status.WARNING)
        createTrip(b, 6, p2, p1, Status.WARNING)
        createTrip(b, 5, p2, p1, Status.WARNING)
        createTrip(b, 6, p2, p1, Status.WARNING)
        createTrip(b, 2, p2, p1, Status.WARNING)
        createTrip(b, 3, p2, p1, Status.WARNING)
        createTrip(b, 1, p2, p1, Status.WARNING)
        createTrip(b, 2, p2, p1, Status.WARNING)
    }

    fun createTrip(b: Boat, crew: Int, p2: Port, p1: Port, status: Status) {
        val t = Trip()
        t.boat = b
        t.crew = crew
        t.destination = p2
        t.origin = p1
        t.status = status

        tripRepository.save(t)
    }

}