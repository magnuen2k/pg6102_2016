package no.kristiania.booking.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
class Trip (

    @Id
    @NotNull
    var id: Long? = 0L,

    @NotNull
    var status: String? = null,

)