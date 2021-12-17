package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty

class TripDto (
    @ApiModelProperty("Id of the trip")
    var tripId: Long? = 0L,

    @ApiModelProperty("")
    var origin: PortDto? = null,

    @ApiModelProperty("")
    var destination: PortDto? = null,

    @ApiModelProperty("")
    var boat: BoatDto? = null,

    @ApiModelProperty("")
    var crew: Int? = 0,

    @ApiModelProperty("")
    var passengers: Int? = 0,

    @ApiModelProperty("")
    var tripYear: Int? = null,

    @ApiModelProperty("")
    var status: String? = null
)