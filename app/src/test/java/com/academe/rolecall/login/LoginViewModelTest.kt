package com.academe.rolecall.login

import com.academe.rolecall.R
import com.academe.rolecall.data.models.UserCredentials
import com.academe.rolecall.data.service.AuthService
import com.academe.rolecall.form.FieldErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: LoginViewModel
    private lateinit var authService: AuthService

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authService = FakeAuthService()
        viewModel = LoginViewModel(authService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
        assertEquals(R.string.error_email_required, state.fieldError?.messageRes)
    }

    @Test
    fun `login with empty password returns error`() {
        val credentials = UserCredentials(email = "test@example.com", password = "")
        viewModel.login(credentials)

        val state = viewModel.state.value
        assertEquals(FieldErrorType.Password, state.fieldError?.type)
        assertEquals(R.string.error_password_required, state.fieldError?.messageRes)
    }

    @Test
    fun `login with valid credentials clears error`() {
        // First set an error
        viewModel.login(UserCredentials("", ""))
        
        // Then login with valid
        viewModel.login(UserCredentials("test@example.com", "password"))

        assertNull(viewModel.state.value.fieldError)
    }

    private class FakeAuthService : AuthService(
        preferences = object : com.academe.rolecall.data.preferences.UserPreferences {
            override val demoModeFlow = kotlinx.coroutines.flow.flowOf(false)
            override val authenticatedFlow = kotlinx.coroutines.flow.flowOf(false)
            override suspend fun setDemoMode(enabled: Boolean) {}
            override suspend fun setAuthenticated(enabled: Boolean) {}
            override suspend fun clear() {}
        },
        sessionRepository = object : com.academe.rolecall.data.repository.AppSessionRepository {
            override suspend fun insert(session: com.academe.rolecall.data.models.AppSession) {}
            override suspend fun delete(session: com.academe.rolecall.data.models.AppSession) {}
            override suspend fun getById(id: Long) = null
            override fun getAll() = kotlinx.coroutines.flow.flowOf(emptyList<com.academe.rolecall.data.models.AppSession>())
            override suspend fun deleteAll() {}
        }
    )
}
