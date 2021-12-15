package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty

class BookingDto (

    @ApiModelProperty("")
    var id: Long? = 0L,

    @ApiModelProperty("")
    var userId: String? = null,

    @ApiModelProperty("")
    var tripId: Long? = 0L,
)