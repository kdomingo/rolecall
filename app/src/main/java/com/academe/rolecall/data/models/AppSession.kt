package com.academe.rolecall.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "app_sessions",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AppSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long?,
    val token: String,
    val expiresOn: Long,
    val createdOn: String?
)