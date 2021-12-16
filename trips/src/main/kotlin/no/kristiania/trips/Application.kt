package no.kristiania.trips

import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@SpringBootApplication
class Application {

    @LoadBalanced
    @Bean
    fun loadBalancedClient() : RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            .select()
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("API for Trips")
            .description("REST service to manage planned trips")
            .version("1.0")
            .build()
    }

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("trip-creation")
    }

    @Bean
    fun weatherFanout(): FanoutExchange {
        return FanoutExchange("notify-weather-change")
    }

    @Bean
    fun queue(): Queue {
        return Queue("weather-change")
    }

    @Bean
    fun binding(weatherFanout: FanoutExchange,
                queue: Queue
    ): Binding {
        return BindingBuilder.bind(queue).to(weatherFanout)
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}