package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class BoatDto (
    @ApiModelProperty("")
    var name: String? = null,

    @ApiModelProperty("")
    var length: Int? = 0,

    @ApiModelProperty("")
    var builder: String? = null,

    @ApiModelProperty("")
    var crewSize: Int? = 0,

    @ApiModelProperty("")
    var maxPassengers: Int? = 0
)