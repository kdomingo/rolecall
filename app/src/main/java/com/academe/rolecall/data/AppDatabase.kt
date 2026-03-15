package com.academe.rolecall.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.academe.rolecall.data.dao.AttendanceDao
import com.academe.rolecall.data.dao.SessionDao
import com.academe.rolecall.data.dao.StudentDao
import com.academe.rolecall.data.dao.UserDao
import com.academe.rolecall.data.models.AppSession
import com.academe.rolecall.data.models.Attendance
import com.academe.rolecall.data.models.Student
import com.academe.rolecall.data.models.User

@Database(entities = [User::class, AppSession::class, Student::class, Attendance::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        const val DATABASE_NAME = "rolecall_db"
    }
}
