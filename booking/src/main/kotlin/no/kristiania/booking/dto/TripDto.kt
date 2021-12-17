package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty

class TripDto (
    @ApiModelProperty("Id of the trip")
    var tripId: Long? = 0L,

    @ApiModelProperty("Origin port of trip")
    var origin: PortDto? = null,

    @ApiModelProperty("Destination port of trip")
    var destination: PortDto? = null,

    @ApiModelProperty("Boat of trip")
    var boat: BoatDto? = null,

    @ApiModelProperty("Crew on trip")
    var crew: Int? = 0,

    @ApiModelProperty("Passengers on trip")
    var passengers: Int? = 0,

    @ApiModelProperty("Year of trip")
    var tripYear: Int? = null,

    @ApiModelProperty("Weather status of trip")
    var status: String? = null
)