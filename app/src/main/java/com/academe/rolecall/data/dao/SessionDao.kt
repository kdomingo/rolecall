package com.academe.rolecall.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.academe.rolecall.data.models.AppSession

@Dao
interface SessionDao {

    @Insert
    fun insert(session: AppSession)

    @Delete
    fun delete(session: AppSession)

    @Query("SELECT * FROM app_sessions WHERE id = :id")
    fun getById(id: Long)
}