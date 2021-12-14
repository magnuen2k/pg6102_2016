package no.kristiania.trips.repository

import no.kristiania.trips.db.Port
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PortRepository : CrudRepository<Port, String>