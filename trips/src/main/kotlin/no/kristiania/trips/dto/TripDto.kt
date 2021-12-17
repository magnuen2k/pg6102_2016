package no.kristiania.trips.dto

import io.swagger.annotations.ApiModelProperty
import no.kristiania.trips.db.Boat
import no.kristiania.trips.db.Port

class TripDto (
    @ApiModelProperty("Id of the trip")
    var tripId: Long? = 0L,

    @ApiModelProperty("Origin port of the trip")
    var origin: Port? = null,

    @ApiModelProperty("Destination port of the trip")
    var destination: Port? = null,

    @ApiModelProperty("Boat of the trip")
    var boat: Boat? = null,

    @ApiModelProperty("Crew on the trip")
    var crew: Int? = 0,

    @ApiModelProperty("Passengers on the trip")
    var passengers: Int? = 0,

    @ApiModelProperty("Year of the trip")
    var tripYear: Int? = null,

    @ApiModelProperty("Weather status of the trip")
    var status: String? = null
)