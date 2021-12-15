package no.kristiania.booking

import no.kristiania.booking.service.TripService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class MOMListener (
    private val tripService: TripService
    ) {

    companion object{
        private val log = LoggerFactory.getLogger(MOMListener::class.java)
    }

    @RabbitListener(queues = ["#{queue.name}"])
    fun receiveFromAMQP(tripId: Long) {

        val ok = tripService.registerTrip(tripId)
        if(ok){
            log.info("Registered new trip via MOM: $tripId")
        }
    }

}