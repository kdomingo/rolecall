package com.academe.rolecall.login

import androidx.lifecycle.ViewModel
import com.academe.rolecall.data.models.UserCredentials
import com.academe.rolecall.form.FieldError
import com.academe.rolecall.form.FieldErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _navigateToDashboard = MutableStateFlow(false)
    val navigateToDashboard = _navigateToDashboard.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    fun togglePasswordVisibility() {
        _state.value = _state.value.copy(showPassword = _state.value.showPassword.not())
    }

    fun login(credentials: UserCredentials) {

        scope.launch {
            _state.emit(_state.value.copy(fieldError = null))
        }

        _state.value = _state.value.copy(
            fieldError = when {
                credentials.email.isBlank() -> FieldError(FieldErrorType.Email, "Email is required")
                credentials.password.isBlank() -> FieldError(
                    FieldErrorType.Password,
                    "Password is required"
                )
                else -> null
            }
        )
    }
}