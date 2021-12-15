package no.kristiania.booking.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank

@Entity
@Table(name="user_data")
class User (
    @Id
    @NotBlank
    var userId: String? = null,

    // Might have a list of all bookings from user?
)