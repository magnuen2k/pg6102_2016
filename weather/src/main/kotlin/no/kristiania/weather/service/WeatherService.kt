package no.kristiania.weather.service

import org.springframework.stereotype.Service

@Service
class WeatherService {

    fun getWeatherStatus(port: String) : String {

        val r = (1..100).random()

        if(r <= 50) {
            return WeatherStatus.NORMAL.toString()
        }

        if(r in 70 downTo 49) {
            return WeatherStatus.WARNING.toString()
        }

        if(r in 71..85) {
            return WeatherStatus.RISKY.toString()
        }

        return WeatherStatus.HAZARD.toString()

    }
}

enum class WeatherStatus {
    NORMAL,
    WARNING,
    RISKY,
    HAZARD
}