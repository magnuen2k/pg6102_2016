package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class PortDto (
    @ApiModelProperty("")
    var name: String? = null,

    @ApiModelProperty("")
    var maxBoats: Int? = 0
)