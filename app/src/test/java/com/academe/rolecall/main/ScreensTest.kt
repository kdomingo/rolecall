package com.academe.rolecall.main

import org.junit.Assert.assertEquals
import org.junit.Test

class ScreensTest {

    @Test
    fun `verify all screen enum constants exist`() {
        val expectedScreens = listOf("Login", "SignUp", "Dashboard", "Profile")
        val actualScreens = Screens.entries.map { it.name }
        
        assertEquals("Screens enum should have 4 constants", 4, actualScreens.size)
        assertEquals("Screens enum constants should match expected names", expectedScreens, actualScreens)
    }

    @Test
    fun `verify screen constant values`() {
        assertEquals(Screens.Login, Screens.valueOf("Login"))
        assertEquals(Screens.SignUp, Screens.valueOf("SignUp"))
        assertEquals(Screens.Dashboard, Screens.valueOf("Dashboard"))
        assertEquals(Screens.Profile, Screens.valueOf("Profile"))
    }
}
