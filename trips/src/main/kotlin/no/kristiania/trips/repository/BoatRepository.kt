package no.kristiania.trips.repository

import no.kristiania.trips.db.Boat
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoatRepository : CrudRepository<Boat, String>