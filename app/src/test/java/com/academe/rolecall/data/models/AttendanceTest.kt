package com.academe.rolecall.data.models

import org.junit.Test
import java.lang.IllegalArgumentException

class AttendanceTest {

    @Test(expected = IllegalArgumentException::class)
    fun `creating attendance with invalid user id throws exception`() {
        Attendance(userId = -5L, date = "2023-10-27")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating attendance with blank date throws exception`() {
        Attendance(userId = 1L, date = "")
    }
}
