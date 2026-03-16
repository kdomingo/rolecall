package com.academe.rolecall.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academe.rolecall.data.service.AttendanceService
import com.academe.rolecall.data.service.StudentService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceService: AttendanceService,
    private val studentService: StudentService
) : ViewModel() {

    private val _state = MutableStateFlow(AttendanceUiState())
    val state = _state.asStateFlow()

    init {
        loadDates()
        loadStudents()
    }

    private fun loadDates() {
        viewModelScope.launch {
            attendanceService.getAllAttendanceDates().collect { dates ->
                _state.update { it.copy(dates = dates) }
                if (dates.isNotEmpty() && _state.value.selectedDate == null) {
                    selectDate(dates.first())
                }
            }
        }
    }

    private fun loadStudents() {
        viewModelScope.launch {
            studentService.getStudents().collect { students ->
                _state.update { it.copy(students = students) }
            }
        }
    }

    fun selectDate(date: String) {
        _state.update { it.copy(selectedDate = date, isLoading = true) }
        viewModelScope.launch {
            attendanceService.getAttendanceForDate(date).collect { attendance ->
                _state.update { it.copy(attendanceList = attendance, isLoading = false) }
            }
        }
    }
}
