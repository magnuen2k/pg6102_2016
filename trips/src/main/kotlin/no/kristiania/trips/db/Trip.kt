package no.kristiania.trips.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class Trip (
    @Id
    @GeneratedValue
    var tripId: Long? = 0L,

    @NotNull
    @ManyToOne
    var origin: Port? = null,

    @NotNull
    @ManyToOne
    var destination: Port? = null,

    @NotNull
    @ManyToOne
    var boat: Boat? = null,

    @NotNull
    var crew: Int? = 0,

    @NotNull
    var status: Status? = null
)

enum class Status {
    WARNING,
}