package com.academe.rolecall.data.repository

import com.academe.rolecall.data.models.Attendance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class DummyAttendanceRepositoryImpl : AttendanceRepository {
    private val attendanceList = MutableStateFlow<List<Attendance>>(
        listOf(
            Attendance(1, 1, "2023-10-25"),
            Attendance(2, 2, "2023-10-25"),
            Attendance(3, 3, "2023-10-25"),
            Attendance(4, 1, "2023-10-26"),
            Attendance(5, 4, "2023-10-26"),
            Attendance(6, 5, "2023-10-26")
        )
    )

    override fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>> {
        return attendanceList.map { list -> list.filter { it.userId == studentId } }
    }

    override fun getAttendanceForDate(date: String): Flow<List<Attendance>> {
        return attendanceList.map { list -> list.filter { it.date == date } }
    }

    override fun getAllAttendanceDates(): Flow<List<String>> {
        return attendanceList.map { list -> list.map { it.date }.distinct().sortedDescending() }
    }

    override suspend fun insert(attendance: Attendance) {
        attendanceList.update { list -> 
            val newList = list.toMutableList()
            newList.add(attendance.copy(id = (list.maxOfOrNull { it.id } ?: 0) + 1))
            newList
        }
    }

    override suspend fun deleteAttendance(studentId: Long, date: String) {
        attendanceList.update { list -> 
            list.filterNot { it.userId == studentId && it.date == date }
        }
    }
}
