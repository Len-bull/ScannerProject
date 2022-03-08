package com.aisier.network.entity

import java.io.Serializable

open class ApiResponse<T>(
        open val data: T? = null,
        open val code: Int? = null,
        open val status: String? = null,
        open val error: Throwable? = null,
) : Serializable {
    val isSuccess: Boolean
        get() = code == 200
}

data class ApiSuccessResponse<T>(val response: T) : ApiResponse<T>(data = response)

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiFailedResponse<T>(override val code: Int?, override val status: String?) : ApiResponse<T>(code = code, status = status)

data class ApiErrorResponse<T>(val throwable: Throwable) : ApiResponse<T>(error = throwable)
