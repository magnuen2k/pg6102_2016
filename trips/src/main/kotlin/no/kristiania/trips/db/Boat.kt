package no.kristiania.trips.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Boat (

    @Id
    @NotBlank
    var name: String? = null,

    @NotNull
    var length: Int? = 0,

    @NotBlank
    var builder: String? = null,

    @NotNull
    var crewSize: Int? = 0,

    @NotNull
    var maxPassengers: Int? = 0

)