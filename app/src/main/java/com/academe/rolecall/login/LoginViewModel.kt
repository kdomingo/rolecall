package com.academe.rolecall.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academe.rolecall.R
import com.academe.rolecall.data.models.UserCredentials
import com.academe.rolecall.data.service.AuthService
import com.academe.rolecall.form.FieldError
import com.academe.rolecall.form.FieldErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _navigateToDashboard = MutableStateFlow(false)
    val navigateToDashboard = _navigateToDashboard.asStateFlow()

    fun togglePasswordVisibility() {
        _state.update { it.copy(showPassword = !it.showPassword) }
    }

    fun login(credentials: UserCredentials) {
        _state.update { it.copy(fieldError = null, loginError = null, isLoading = true) }

        val fieldError = when {
            credentials.email.isBlank() -> FieldError(FieldErrorType.Email, R.string.error_email_required)
            credentials.password.isBlank() -> FieldError(FieldErrorType.Password, R.string.error_password_required)
            else -> null
        }

        if (fieldError != null) {
            _state.update { it.copy(fieldError = fieldError, isLoading = false) }
            return
        }

        viewModelScope.launch {
            authService.login(credentials).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _navigateToDashboard.value = true
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        loginError = LoginError(
                            type = LoginErrorType.Unknown, // Defaulting to Unknown, could be refined based on 'e'
                            message = e.message ?: "Login failed"
                        )
                    )
                }
            }
        }
    }
}
