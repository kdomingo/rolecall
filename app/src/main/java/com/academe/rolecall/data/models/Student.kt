package com.academe.rolecall.data.models

data class Student(
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
