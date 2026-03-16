package com.academe.rolecall.login

enum class LoginErrorType {
    InvalidCredentials,
    NetworkError,
    Unknown
}

data class LoginError(
    val type: LoginErrorType,
    val message: String
)
