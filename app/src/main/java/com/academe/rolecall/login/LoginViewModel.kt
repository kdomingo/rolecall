package com.academe.rolecall.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academe.rolecall.R
import com.academe.rolecall.data.models.DemoAccount
import com.academe.rolecall.data.models.UserCredentials
import com.academe.rolecall.data.preferences.UserPreferences
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
    private val preferences: UserPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _navigateToDashboard = MutableStateFlow(false)
    val navigateToDashboard = _navigateToDashboard.asStateFlow()

    fun togglePasswordVisibility() {
        _state.update { it.copy(showPassword = !it.showPassword) }
    }

    fun login(credentials: UserCredentials) {
        _state.update { it.copy(fieldError = null) }

        val fieldError = when {
            credentials.email.isBlank() -> FieldError(FieldErrorType.Email, R.string.error_email_required)
            credentials.password.isBlank() -> FieldError(FieldErrorType.Password, R.string.error_password_required)
            else -> null
        }

        if (fieldError != null) {
            _state.update { it.copy(fieldError = fieldError) }
            return
        }

        viewModelScope.launch {
            val demo = DemoAccount()
            preferences.setDemoMode(credentials.email == demo.email && credentials.password == demo.password)
            preferences.setAuthenticated(true)
            _navigateToDashboard.value = true
        }
    }
}
