package no.kristiania.trips.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Port (

    @Id
    @NotBlank
    var name: String? = null,

    @NotNull
    var maxBoats: Int? = 0

)

