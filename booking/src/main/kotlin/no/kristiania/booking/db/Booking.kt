package no.kristiania.booking.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class Booking (

    @Id
    @GeneratedValue
    var id: Long? = 0L,

    @NotNull
    @ManyToOne
    var user: User,

    @NotNull
    @ManyToOne
    var trip: Trip,

    @NotNull
    var ongoing: Status? = null

)

enum class Status {
    ONGOING,
    PLANNED
}