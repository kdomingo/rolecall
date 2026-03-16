package com.academe.rolecall.data.service

import com.academe.rolecall.data.models.Attendance
import com.academe.rolecall.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceService @Inject constructor(
    private val repository: AttendanceRepository
) {
    fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>> {
        return repository.getAttendanceForStudent(studentId)
    }

    fun getAttendanceForDate(date: String): Flow<List<Attendance>> {
        return repository.getAttendanceForDate(date)
    }

    fun getAllAttendanceDates(): Flow<List<String>> {
        return repository.getAllAttendanceDates()
    }

    suspend fun markAttendance(studentId: Long, date: String) {
        val attendance = Attendance(userId = studentId, date = date)
        repository.insert(attendance)
    }

    suspend fun removeAttendance(studentId: Long, date: String) {
        repository.deleteAttendance(studentId, date)
    }
}
