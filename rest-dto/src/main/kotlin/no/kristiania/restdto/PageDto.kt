package no.kristiania.restdto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.jetbrains.annotations.NotNull

/* Code copied from class */
@ApiModel(description = "Paginated list of resources")
class PageDto<T>(

    @ApiModelProperty("The data contained in the page")
    @get:NotNull
    var list: List<T> = listOf(),

    @ApiModelProperty("Link to the next page, if it exists")
    var next: String? = null
)