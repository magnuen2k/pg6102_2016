package no.kristiania.trips.db

import no.kristiania.trips.repository.BoatRepository
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Service
@Transactional
class DefaultDataService (
    val tripRepository: TripRepository,
    val boatRepository: BoatRepository,
    val portRepository: PortRepository,
    private val rabbit: RabbitTemplate,
    private val fanout: FanoutExchange
    ) {

    @PostConstruct
    fun init() {

        // If there is not data, crete it
        if(boatRepository.count() == 0L && portRepository.count() == 0L && tripRepository.count() == 0L) {
            // Create boats
            val test = createBoat("Test", 32, "Test2", 6, 20)

            // Create ports
            val port1 = createPort("port1", 50)
            val port2 = createPort("port2", 100)

            // Create default trips if there is none
            createTrip(test, 2, port2, port1, Status.WARNING, 2001)
            createTrip(test, 4, port2, port1, Status.WARNING, 2005)
            createTrip(test, 5, port2, port1, Status.WARNING, 2015)
            createTrip(test, 6, port2, port1, Status.WARNING, 2021)
            createTrip(test, 5, port2, port1, Status.WARNING, 2020)
            createTrip(test, 6, port2, port1, Status.WARNING, 2019)
            createTrip(test, 2, port2, port1, Status.WARNING, 2018)
            createTrip(test, 3, port2, port1, Status.WARNING, 2020)
            createTrip(test, 1, port2, port1, Status.WARNING, 2008)
            createTrip(test, 2, port2, port1, Status.WARNING, 2009)
        }
    }

    fun createTrip(b: Boat, crew: Int, p2: Port, p1: Port, status: Status, tripYear: Int) {
        val t = tripRepository.save(Trip( origin = p1, destination = p2, boat = b, crew = crew, tripYear = tripYear, status = status))

        // To make sure trips are created and handled in booking api
        rabbit.convertAndSend(fanout.name, "", t.tripId!!)
    }

    fun createPort(name: String, maxBoats: Int): Port {
        return portRepository.save(Port(name, maxBoats))
    }

    fun createBoat(name: String, length: Int, builder: String, crewSize: Int, maxPassengers: Int): Boat {
        return boatRepository.save(Boat(name, length, builder, crewSize, maxPassengers))
    }

}