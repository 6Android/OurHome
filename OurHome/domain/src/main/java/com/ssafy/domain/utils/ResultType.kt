package com.ssafy.domain.utils

sealed class ResultType<out T> {
    object Uninitialized : ResultType<Nothing>()

    object Loading : ResultType<Nothing>()

    object Empty : ResultType<Nothing>()

    data class Success<T>(val data: T) : ResultType<T>()

    data class Fail<T>(val data: T) : ResultType<T>()

    data class Error(val exception: Exception?) : ResultType<Nothing>()
}
