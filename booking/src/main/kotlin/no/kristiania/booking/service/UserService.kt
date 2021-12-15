package no.kristiania.booking.service

import no.kristiania.booking.db.User
import no.kristiania.booking.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService (
    private val userRepository: UserRepository
    ) {

    // Should create user here when a user signs up in Auth with AMQP
    fun createUser(userId: String) : User {
        val user = User()
        user.userId = userId
        userRepository.save(user)
        return user
    }

    fun getUserId(userId: String) : User? {
        return userRepository.findWithLock(userId)
    }


}