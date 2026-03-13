package com.academe.rolecall.data.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "app_sessions",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
    ]
)
data class AppSession(
    val id: Long,
    val userId: Long?,
    val token: String,
    val expiresAt: Long,
    val createdAt: String?
)