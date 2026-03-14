package com.academe.rolecall.login

import com.academe.rolecall.form.FieldError

data class LoginUiState(
    val showPassword: Boolean = false,
    val fieldError: FieldError? = null
)
