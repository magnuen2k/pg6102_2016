package no.kristiania.booking.service

import com.google.gson.Gson
import no.kristiania.booking.db.Trip
import no.kristiania.booking.dto.TripDto
import no.kristiania.booking.repository.TripRepository
import no.kristiania.booking.repository.UserRepository
import no.kristiania.restdto.WrappedResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import javax.annotation.PostConstruct

@Service
@Transactional
class TripService (
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository,
    private val client: RestTemplate,
    private val circuitBreakerFactory: Resilience4JCircuitBreakerFactory
    ) {

    companion object{
        private val log = LoggerFactory.getLogger(TripService::class.java)
    }

    @Value("\${tripServiceAddress}")
    private lateinit var tripServiceAddress: String

    private lateinit var cb: CircuitBreaker

    @PostConstruct
    fun init(){
        cb = circuitBreakerFactory.create("circuitBreakerToTrips")
    }

    fun getTrip(tripId: Long) : Trip? {
        return tripRepository.findWithLock(tripId)
    }

    fun registerTrip(tripId: Long): Boolean {
        // Check if trip exists first!!
        val trip = Trip()
        trip.id = tripId
        tripRepository.save(trip)
        return true
    }

    fun getAllTripsByUserId(userId: String): List<TripDto>? {
        val uri = UriComponentsBuilder
            .fromUriString("http://${tripServiceAddress.trim()}/api/trips/byIds")
            .build().toUri()

        val user = userRepository.findWithLock(userId)

        if(user?.trips == null) {
            return null
        }

        val trips: MutableList<Long> = mutableListOf()

        user.trips.forEach { booking ->
            trips.add(booking.trip.id!!)
        }

        val jsonList = Gson().toJson(trips)
        log.info(jsonList)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val data = HttpEntity(jsonList, headers)

        val response = cb.run(
            {
                client.exchange(
                    uri,
                    HttpMethod.POST,
                    data,
                    object : ParameterizedTypeReference<WrappedResponse<List<TripDto>>>() {}
                )
            },
            { err ->
                log.error("Failed to fetch data with error: ${err.message}")
                null
            }
        ) ?: return null

        log.info("RESPONSE")

        if(response.statusCodeValue != 200) {
            log.error("There was an error fetching the data")
        }

        return response.body.data
    }
}