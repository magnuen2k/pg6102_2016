package no.kristiania.trips.service

import no.kristiania.restdto.WrappedResponse
import no.kristiania.trips.db.Port
import no.kristiania.trips.repository.PortRepository
import no.kristiania.trips.repository.TripRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import javax.annotation.PostConstruct

@Service
class PortService(
    private val portRepository: PortRepository,
    private val tripRepository: TripRepository,
    private val client: RestTemplate,
    private val circuitCreakerFactory: Resilience4JCircuitBreakerFactory
) {

    companion object {
        private val log = LoggerFactory.getLogger(PortService::class.java)
    }

    @Value("\${weatherServiceAddress}")
    private lateinit var weatherServiceAddress: String

    private lateinit var cb: CircuitBreaker

    @PostConstruct
    fun init(){
        cb = circuitCreakerFactory.create("circuitBreakerToWeather")
    }

    fun getPorts(): MutableIterable<Port> {
        return portRepository.findAll()
    }

    fun updateWeather() {
        val ports = portRepository.findAll()


        for (port in ports) {
            val uri = UriComponentsBuilder
                .fromUriString("http://${weatherServiceAddress.trim()}/api/weather/${port.name}")
                .build().toUri()

            val response = cb.run(
                {
                    client.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        object : ParameterizedTypeReference<WrappedResponse<String>>() {})
                },
                { e ->
                    log.error("Failed to fetch data: ${e.message}")
                    null
                }
            ) ?: return

            val trips = tripRepository.findAll()

            for (trip in trips) {
                val t = tripRepository.findByIdOrNull(trip.tripId!!)!!
                if(t.destination?.name == port.name) {
                    t.status = response.body.data
                    tripRepository.save(t)
                }
            }
        }
    }
}