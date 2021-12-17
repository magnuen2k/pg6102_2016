package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty

class PortDto (
    @ApiModelProperty("Port name")
    var name: String? = null,

    @ApiModelProperty("Max boats on port")
    var maxBoats: Int? = 0
)