package com.academe.rolecall.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.academe.rolecall.data.models.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance WHERE userId = :userId")
    fun getAttendanceForUser(userId: Long): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE date = :date")
    fun getAttendanceForDate(date: String): Flow<List<Attendance>>

    @Query("SELECT DISTINCT date FROM attendance ORDER BY date DESC")
    fun getAllAttendanceDates(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: Attendance)

    @Query("DELETE FROM attendance WHERE userId = :userId AND date = :date")
    suspend fun deleteAttendance(userId: Long, date: String)
}
