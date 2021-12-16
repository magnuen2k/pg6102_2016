package no.kristiania.booking

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
        val restTemplate = RestTemplate()
        val messageConverter = MappingJackson2HttpMessageConverter()
        messageConverter.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        restTemplate.messageConverters.add(messageConverter)
        return restTemplate
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
            .title("API for Booking")
            .description("REST service to manage booking of trips")
            .version("1.0")
            .build()
    }

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("trip-creation")
    }

    @Bean
    fun queue(): Queue {
        return Queue("booking-init-booking-service")
    }

    @Bean
    fun binding(fanout: FanoutExchange,
                queue: Queue
    ): Binding {
        return BindingBuilder.bind(queue).to(fanout)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}