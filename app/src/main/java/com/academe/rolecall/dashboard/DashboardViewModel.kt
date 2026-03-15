package com.academe.rolecall.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academe.rolecall.data.models.Student
import com.academe.rolecall.data.preferences.UserPreferences
import com.academe.rolecall.data.service.AttendanceService
import com.academe.rolecall.data.service.AuthService
import com.academe.rolecall.data.service.StudentService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val studentService: StudentService,
    private val attendanceService: AttendanceService,
    private val authService: AuthService,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state = _state.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        viewModelScope.launch {
            preferences.demoModeFlow.collect { isDemoMode ->
                _state.update { it.copy(isDemoMode = isDemoMode) }
            }
        }
        loadStudents()
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
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                }
                .collect { students ->
                    _state.update { it.copy(students = students, isLoading = false) }
                }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentService.deleteStudent(student)
        }
    }

    fun markPresent(student: Student) {
        viewModelScope.launch {
            val date = dateFormat.format(Date())
            student.id?.let {
                attendanceService.markAttendance(it, date)
            }
        }
    }
}
