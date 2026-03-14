package com.academe.rolecall.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.academe.rolecall.data.models.AppSession
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: AppSession)

    @Delete
    suspend fun delete(session: AppSession)

    @Query("SELECT * FROM app_sessions WHERE id = :id")
    suspend fun getById(id: Long): AppSession?

    @Query("SELECT * FROM app_sessions")
    fun getAll(): Flow<List<AppSession>>

    @Query("DELETE FROM app_sessions")
    suspend fun deleteAll()
}