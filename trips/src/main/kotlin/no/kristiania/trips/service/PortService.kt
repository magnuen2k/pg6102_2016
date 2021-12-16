package no.kristiania.trips.service

import no.kristiania.trips.db.Port
import no.kristiania.trips.repository.PortRepository
import org.springframework.stereotype.Service

@Service
class PortService(
    private val portRepository: PortRepository
) {

    fun getPorts(): MutableIterable<Port> {
        return portRepository.findAll()
    }

    fun updateWeather() {
        val ports = portRepository.findAll()

        for (port in ports) {

        }
    }
}