package no.kristiania.trips.dto

import io.swagger.annotations.ApiModelProperty

class PatchTripDto (
    @ApiModelProperty("")
    var passengers: Int? = 0,

    @ApiModelProperty("")
    var crew: Int? = 0
)