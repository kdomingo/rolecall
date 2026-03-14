package com.academe.rolecall.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academe.rolecall.data.preferences.UserPreferences
import com.academe.rolecall.data.service.AuthService
import com.academe.rolecall.data.service.StudentService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val studentService: StudentService,
    private val authService: AuthService,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.demoModeFlow.collect { isDemoMode ->
                _state.update { it.copy(isDemoMode = isDemoMode) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
        }
    }

    fun loadStudents() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            studentService.getStudents()
                .onSuccess { students ->
                    _state.update { it.copy(students = students, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                }
        }
    }
}
