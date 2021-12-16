package no.kristiania.weather.service

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service
import java.util.*
import kotlin.concurrent.schedule

@Service
class WeatherUpdateNotificationService(
        private val rabbit: RabbitTemplate,
        private val fanout: FanoutExchange
    ) : CommandLineRunner {

    companion object{
        private val log = LoggerFactory.getLogger(WeatherUpdateNotificationService::class.java)
    }

    override fun run(vararg args: String?) {
        log.info("Started weather service")

        Timer().schedule(2000) {
            fireWeatherChange()
        }
    }

    fun fireWeatherChange() {
        log.info("timer fired!")

        rabbit.convertAndSend(fanout.name, "", "Weather has changed")
        // Fire next weather change in 2 seconds
        Timer().schedule(2000) {
            fireWeatherChange()
        }
    }

}