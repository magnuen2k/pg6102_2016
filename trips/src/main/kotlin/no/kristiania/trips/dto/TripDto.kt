package no.kristiania.trips.dto

import io.swagger.annotations.ApiModelProperty
import no.kristiania.trips.db.Boat
import no.kristiania.trips.db.Port
import no.kristiania.trips.db.Status

class TripDto (
    @ApiModelProperty("Id of the trip")
    var tripId: Long? = 0L,

    @ApiModelProperty("")
    var origin: Port? = null,

    @ApiModelProperty("")
    var destination: Port? = null,

    @ApiModelProperty("")
    var boat: Boat? = null,

    @ApiModelProperty("")
    var crew: Int? = 0,

    @ApiModelProperty("")
    var passengers: Int? = 0,

    @ApiModelProperty("")
    var tripYear: Int? = null,

    @ApiModelProperty("")
    var status: Status? = null
)