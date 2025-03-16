package org.tidy.feature_clients.core

sealed class Result<out T, out E> {
    data class Success<T>(val data: T) : Result<T, Nothing>()
    data class Failure<E>(val error: E) : Result<Nothing, E>()

    companion object {
        fun <T> success(data: T): Result<T, Nothing> = Success(data)
        fun <E> failure(error: E): Result<Nothing, E> = Failure(error)
    }
}