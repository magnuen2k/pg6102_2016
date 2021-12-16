package no.kristiania.e2etests.dto

import io.swagger.annotations.ApiModelProperty

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