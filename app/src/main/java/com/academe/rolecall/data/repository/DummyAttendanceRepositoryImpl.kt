package com.academe.rolecall.data.repository

import com.academe.rolecall.data.models.Attendance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class DummyAttendanceRepositoryImpl : AttendanceRepository {
    private val attendanceList = MutableStateFlow<List<Attendance>>(emptyList())

    override fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>> {
        return attendanceList.map { list -> list.filter { it.userId == studentId } }
    }

    override suspend fun insert(attendance: Attendance) {
        val newList = attendanceList.value.toMutableList()
        newList.add(attendance)
        attendanceList.value = newList
    }

    override suspend fun deleteAttendance(studentId: Long, date: String) {
        val newList = attendanceList.value.toMutableList()
        newList.removeAll { it.userId == studentId && it.date == date }
        attendanceList.value = newList
    }
}
