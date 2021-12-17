package no.kristiania.auth

import javax.validation.constraints.NotBlank

// Copied from class

class AuthDto(
    @get:NotBlank
    var userId : String? = null,

    @get:NotBlank
    var password: String? = null
)