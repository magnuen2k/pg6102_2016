package no.kristiania.trips.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Trip (
    @Id
    @GeneratedValue
    var tripId: Long? = 0L,

    var origin: String? = null,

    var destination: String? = null,

    var boat: String? = null,

    var crew: String? = null,

    var status: String? = null
)