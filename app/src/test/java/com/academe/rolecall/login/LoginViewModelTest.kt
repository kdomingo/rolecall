package com.academe.rolecall.login

import com.academe.rolecall.form.FieldErrorType
import com.academe.rolecall.data.models.UserCredentials
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.state.first()
        assertFalse(state.showPassword)
        assertNull(state.fieldError)
    }

    @Test
    fun `togglePasswordVisibility updates state`() {
        viewModel.togglePasswordVisibility()
        assertTrue(viewModel.state.value.showPassword)

        viewModel.togglePasswordVisibility()
        assertFalse(viewModel.state.value.showPassword)
    }

    @Test
    fun `login with empty email returns error`() {
        val credentials = UserCredentials(email = "", password = "password")
        viewModel.login(credentials)

        val state = viewModel.state.value
        assertEquals(FieldErrorType.Email, state.fieldError?.type)
        assertEquals("Email is required", state.fieldError?.message)
    }

    @Test
    fun `login with empty password returns error`() {
        val credentials = UserCredentials(email = "test@example.com", password = "")
        viewModel.login(credentials)

        val state = viewModel.state.value
        assertEquals(FieldErrorType.Password, state.fieldError?.type)
        assertEquals("Password is required", state.fieldError?.message)
    }

    @Test
    fun `login with valid credentials clears error`() {
        // First set an error
        viewModel.login(UserCredentials("", ""))
        
        // Then login with valid
        viewModel.login(UserCredentials("test@example.com", "password"))

        assertNull(viewModel.state.value.fieldError)
    }
}
