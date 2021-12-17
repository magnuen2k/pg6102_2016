package no.kristiania.trips.dto

import io.swagger.annotations.ApiModelProperty

class PatchTripDto (
    @ApiModelProperty("Passengers to add to trip")
    var passengers: Int? = 0,

    @ApiModelProperty("Crew to add to trip")
    var crew: Int? = 0
)