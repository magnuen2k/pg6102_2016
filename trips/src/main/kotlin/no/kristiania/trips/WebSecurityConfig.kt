package no.kristiania.trips

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails


// Code copied from class, but modified to fit

@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {

        http
            .exceptionHandling().authenticationEntryPoint {req,response,e ->
                response.setHeader("WWW-Authenticate","cookie")
                response.sendError(401)
            }.and()
            .authorizeRequests()
            .antMatchers("/swagger*/**", "/v3/api-docs", "/actuator/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/trips", "/api/trips/ports", "/api/trips/boats").permitAll()
            .antMatchers(HttpMethod.POST, "/api/trips/byIds").permitAll()
            .antMatchers(HttpMethod.PUT, "/api/trips/").authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/trips/{id}").authenticated()
            .antMatchers(HttpMethod.GET, "/api/trips/{id}").authenticated()
            .antMatchers(HttpMethod.PATCH, "/api/trips/{id}").authenticated()
            //.access("hasRole('USER') and @userSecurity.checkId(authentication, #id)")
            .anyRequest().denyAll()
            .and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)

    }

    @Bean
    fun userSecurity() : UserSecurity {
        return UserSecurity()
    }
}

class UserSecurity{

    fun checkId(authentication: Authentication, id: String) : Boolean{

        if(authentication.principal !is UserDetails){
            return false
        }

        val current = (authentication.principal as UserDetails).username

        return current == id
    }
}