package no.kristiania.booking.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class User (
    @Id
    @NotBlank
    var userId: String? = null,

    // Might have a list of all bookings from user?
)