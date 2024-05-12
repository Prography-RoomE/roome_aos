package com.sevenstars.domain.utils

sealed class RoomeResult<out T> {
    data class Success<out T>(val data: T) : RoomeResult<T>()
    data class Failure(val code: Int, val message: String) : RoomeResult<Nothing>()

    fun onSuccess(action: (T) -> Unit): RoomeResult<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    fun onFailure(action: (Int, String) -> Unit): RoomeResult<T> {
        if (this is Failure) {
            action(code, message)
        }
        return this
    }
}