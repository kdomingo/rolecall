package com.academe.rolecall.dashboard

import com.academe.rolecall.data.models.Student
import com.academe.rolecall.form.FieldError

data class DashboardUiState(
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val fieldError: FieldError? = null
)
