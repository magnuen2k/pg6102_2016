package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty

class BoatDto (
    @ApiModelProperty("Boat name")
    var name: String? = null,

    @ApiModelProperty("Boat length")
    var length: Int? = 0,

    @ApiModelProperty("Boat builder name")
    var builder: String? = null,

    @ApiModelProperty("Crew size on boat")
    var crewSize: Int? = 0,

    @ApiModelProperty("Max passengers on boat")
    var maxPassengers: Int? = 0
)