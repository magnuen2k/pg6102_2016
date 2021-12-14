package no.kristiania.trips.dto

import io.swagger.annotations.ApiModelProperty

class TripDto (
    @ApiModelProperty("")
    var origin: String? = null,

    @ApiModelProperty("")
    var destination: String? = null,

    @ApiModelProperty("")
    var boat: String? = null,

    @ApiModelProperty("")
    var crew: Int? = 0,

)