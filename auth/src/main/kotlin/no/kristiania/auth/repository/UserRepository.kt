package no.kristiania.auth.repository

import no.kristiania.auth.db.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String>