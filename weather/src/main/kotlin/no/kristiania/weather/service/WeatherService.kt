package no.kristiania.weather.service

import org.springframework.stereotype.Service

@Service
class WeatherService {

    private var test: Int = 0

    fun getWeatherStatus(port: String) : String {

        test += 1

        if(test > 4) {
            test = 1
        }

        if(test == 1) {
            return WeatherStatus.NORMAL.toString()
        }
        if(test == 2) {
            return WeatherStatus.WARNING.toString()
        }
        if(test == 3) {
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