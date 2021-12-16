package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty

class PortDto (
    @ApiModelProperty("")
    var name: String? = null,

    @ApiModelProperty("")
    var maxBoats: Int? = 0
)