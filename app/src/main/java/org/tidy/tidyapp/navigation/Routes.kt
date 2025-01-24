package org.tidy.tidyapp.navigation
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Login: Route

    @Serializable
    data object Register: Route
    @Serializable
    data object Auth: Route

    @Serializable
    data class Home(val email: String): Route
}
