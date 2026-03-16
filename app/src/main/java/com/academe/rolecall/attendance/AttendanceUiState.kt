package com.academe.rolecall.attendance

import com.academe.rolecall.data.models.Attendance
import com.academe.rolecall.data.models.Student

data class AttendanceUiState(
    val selectedDate: String? = null,
    val dates: List<String> = emptyList(),
    val attendanceList: List<Attendance> = emptyList(),
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
