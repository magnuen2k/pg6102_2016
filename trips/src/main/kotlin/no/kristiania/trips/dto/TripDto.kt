package no.kristiania.trips.dto

import io.swagger.annotations.ApiModelProperty

class TripDto (
    @ApiModelProperty("Id of the trip")
    var tripId: Long? = 0L,

    @ApiModelProperty("")
    var origin: String? = null,

    @ApiModelProperty("")
    var destination: String? = null,

    @ApiModelProperty("")
    var boat: String? = null,

    @ApiModelProperty("")
    var crew: Int? = 0,

    @ApiModelProperty("")
    var passengers: Int? = 0,

    @ApiModelProperty("")
    var tripYear: Int? = null

)