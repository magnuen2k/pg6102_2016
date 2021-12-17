package no.kristiania.auth.repository

import no.kristiania.auth.db.User
import org.springframework.data.repository.CrudRepository

// Copied from class
interface UserRepository : CrudRepository<User, String>