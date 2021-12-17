package no.kristiania.auth.db

import no.kristiania.auth.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// Copied from class

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
){

    fun createUser(username: String, password: String, roles: Set<String> = setOf()) : Boolean{

        try {
            val hash = passwordEncoder.encode(password)

            if (userRepository.existsById(username)) {
                return false
            }

            val user = User(username, hash, roles.map{"ROLE_$it"}.toSet())

            userRepository.save(user)

            return true
        } catch (e: Exception){
            return false
        }
    }

}