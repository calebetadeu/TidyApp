package org.tidy.feature_clients.core

import org.tidy.core.domain.DataError

suspend fun <T> safeCall(block: suspend () -> T): Result<T, DataError.Remote> {
    return try {
        Result.success(block())
    } catch (exception: Exception) {
        Result.failure(DataError.Remote(exception))
    }
}