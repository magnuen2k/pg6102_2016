package no.kristiania.booking.db

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.NotNull

@Entity
class Trip (

    @Id
    @NotNull
    var id: Long? = 0L,

    @NotNull
    var status: String? = null,

    @OneToMany(mappedBy = "trip", cascade = [(CascadeType.MERGE)])
    var bookings: MutableList<Booking> = mutableListOf()

)