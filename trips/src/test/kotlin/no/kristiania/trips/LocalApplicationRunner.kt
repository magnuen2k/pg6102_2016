package no.kristiania.trips

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, "--spring.profiles.active=test")
}