package no.kristiania.booking.db

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name="user_data")
class User (
    @Id
    @NotBlank
    var userId: String? = null,

    @OneToMany(mappedBy = "user", cascade = [(CascadeType.MERGE)])
    var trips: MutableList<Booking> = mutableListOf()
)