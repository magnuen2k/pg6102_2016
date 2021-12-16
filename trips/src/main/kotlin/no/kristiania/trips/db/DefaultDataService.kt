package no.kristiania.trips.db

import no.kristiania.trips.repository.BoatRepository
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DefaultDataService (
    val tripRepository: TripRepository,
    val boatRepository: BoatRepository,
    val portRepository: PortRepository,
    private val rabbit: RabbitTemplate,
    private val fanout: FanoutExchange
    ) : CommandLineRunner {

    override fun run(vararg args: String?) {

        // If there is not data, crete it
        if(boatRepository.count() == 0L && portRepository.count() == 0L && tripRepository.count() == 0L) {
            // Create boats
            val frodo = createBoat("Frodo", 23, "Tolkien", 5, 10)
            val sam = createBoat("Sam", 25, "Tolkien", 8, 15)
            val pippin = createBoat("Pippin", 30, "Tolkien", 9, 20)
            val merry = createBoat("HMS Victory", 69, "Chatham Dockyard", 80, 500)
            val test = createBoat("Test", 32, "Test2", 6, 20)

            // Create ports
            val felix = createPort("Port of Felixstowe", 300)
            val southampton = createPort("Port of Southampton", 250)
            val london = createPort("Port of London", 200)
            val immingham = createPort("Port of Immingham", 150)
            val liverpool = createPort("Port of Liverpool", 300)
            val lowestoft = createPort("Port of Lowestoft", 230)
            val port1 = createPort("port1", 50)
            val port2 = createPort("port2", 100)

            // Create default trips if there is none
            createTrip(frodo, 2, felix, immingham, "default", 2016)
            createTrip(frodo, 4, lowestoft, liverpool, "default", 2015)
            createTrip(sam, 6, london, felix, "default", 2020)
            createTrip(sam, 5, southampton, london, "default", 2012)
            createTrip(pippin, 6, liverpool, lowestoft, "default", 2021)
            createTrip(pippin, 2, london, felix, "default", 2014)
            createTrip(merry, 5, lowestoft, southampton, "default", 2016)
            createTrip(merry, 3, southampton, immingham, "default", 2020)
            createTrip(test, 1, port2, port1, "default", 2001)
            createTrip(test, 2, port1, port2, "default", 2002)
        }
    }

    fun createTrip(b: Boat, crew: Int, p2: Port, p1: Port, status: String, tripYear: Int) {
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