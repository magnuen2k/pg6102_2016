package no.kristiania.booking.dto

import io.swagger.annotations.ApiModelProperty

class BookingDto (

    @ApiModelProperty("Booking id")
    var id: Long? = 0L,

    @ApiModelProperty("UserId attached to booking")
    var userId: String? = null,

    @ApiModelProperty("Trip attached to booking")
    var tripId: Long? = 0L,
)