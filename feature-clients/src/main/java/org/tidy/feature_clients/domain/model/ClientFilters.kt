package org.tidy.feature_clients.domain.model

data class ClientFilters(
    val razaoSocial: String = "",
    val cidade: String = "",
    val estado: String = ""
)
