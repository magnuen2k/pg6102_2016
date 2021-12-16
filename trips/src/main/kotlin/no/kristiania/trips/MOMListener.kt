package no.kristiania.trips

import no.kristiania.trips.service.PortService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class MOMListener (
    private val portService: PortService
) {

    companion object{
        private val log = LoggerFactory.getLogger(MOMListener::class.java)
    }

    @RabbitListener(queues = ["#{queue.name}"])
    fun receiveFromAMQP(message: String) {
        portService.updateWeather()
    }

}