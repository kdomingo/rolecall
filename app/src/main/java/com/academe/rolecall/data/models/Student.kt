package com.academe.rolecall.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val email: String,
    val rollNumber: String
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(email.contains("@")) { "Invalid email address" }
        require(rollNumber.isNotBlank()) { "Roll number cannot be blank" }
    }
}
