package com.academe.rolecall.data.models

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val email: String,
    val password: String,
)