package com.academe.rolecall.data.models

import org.junit.Test
import java.lang.IllegalArgumentException

class AppSessionTest {

    @Test(expected = IllegalArgumentException::class)
    fun `creating session with blank token throws exception`() {
        AppSession(userId = 1L, token = "", expiresOn = 1000L, createdOn = "2023-10-27 10:30:00")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating session with zero expiry time throws exception`() {
        AppSession(userId = 1L, token = "some-token", expiresOn = 0L, createdOn = "2023-10-27 10:30:00")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating session with negative expiry time throws exception`() {
        AppSession(userId = 1L, token = "some-token", expiresOn = -100L, createdOn = "2023-10-27 10:30:00")
    }
}
