package org.tidy.core.domain

//sealed interface DataError: Error {
//    enum class Remote: DataError {
//        REQUEST_TIMEOUT,
//        TOO_MANY_REQUESTS,
//        NO_INTERNET,
//        SERVER,
//        SERIALIZATION,
//        UNKNOWN,
//        HTTP_ERROR
//    }
//
//    enum class Local: DataError {
//        DISK_FULL,
//        UNKNOWN
//    }
//}

sealed class DataError {
    data class Remote(val exception: Exception) : DataError()
    // Outros tipos (ex.: Local) podem ser adicionados se necessário
}