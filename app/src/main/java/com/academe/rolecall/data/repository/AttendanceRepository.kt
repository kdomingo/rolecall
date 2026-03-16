package com.academe.rolecall.data.repository

import com.academe.rolecall.data.dao.AttendanceDao
import com.academe.rolecall.data.models.Attendance
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>>
    fun getAttendanceForDate(date: String): Flow<List<Attendance>>
    fun getAllAttendanceDates(): Flow<List<String>>
    suspend fun insert(attendance: Attendance)
    suspend fun deleteAttendance(studentId: Long, date: String)
}

class AttendanceRepositoryImpl(private val attendanceDao: AttendanceDao) : AttendanceRepository {
    override fun getAttendanceForStudent(studentId: Long) = attendanceDao.getAttendanceForUser(studentId)
    override fun getAttendanceForDate(date: String) = attendanceDao.getAttendanceForDate(date)
    override fun getAllAttendanceDates() = attendanceDao.getAllAttendanceDates()
    override suspend fun insert(attendance: Attendance) = attendanceDao.insert(attendance)
    override suspend fun deleteAttendance(studentId: Long, date: String) = attendanceDao.deleteAttendance(studentId, date)
}
