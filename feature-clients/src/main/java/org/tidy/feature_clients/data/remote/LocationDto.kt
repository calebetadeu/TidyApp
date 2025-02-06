package org.tidy.feature_clients.data.remote

import com.google.firebase.database.PropertyName


data class LocationDto(
    @get:PropertyName("cidades") @set:PropertyName("cidades")
    var listaCidades: List<String> = emptyList()
) {
    var estado: String = ""
}
