package com.example.sadekahvefal.utils

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()

    object Empty : ApiState<Nothing>()

    data class Success<out T>(val data: T) : ApiState<T>()

    data class SuccessMessage(
        val successMessage: String?
    ) : ApiState<Nothing>()

    data class Failure(
        val errorMessage: String?
    ) : ApiState<Nothing>()
}
