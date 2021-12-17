package no.kristiania.booking

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

// Copied from class, but modified to fit

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
            .antMatchers("/api/booking/**").authenticated()
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