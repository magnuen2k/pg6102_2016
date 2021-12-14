package no.kristiania.trips.repository

import no.kristiania.trips.db.Trip
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TripRepository : CrudRepository<Trip, Long>