package com.academe.rolecall.login

import com.academe.rolecall.form.FieldError

data class LoginUiState(
    val isLoading: Boolean = false,
    val showPassword: Boolean = false,
    val fieldError: FieldError? = null,
    val loginError: LoginError? = null
)
