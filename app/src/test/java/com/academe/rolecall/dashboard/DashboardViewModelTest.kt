package com.academe.rolecall.dashboard

import com.academe.rolecall.data.models.Student
import com.academe.rolecall.data.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class DashboardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: DashboardViewModel
    private lateinit var repository: FakeStudentRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeStudentRepository()
        viewModel = DashboardViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadStudents failure sets error state`() = runTest {
        val errorMessage = "Network Error"
        repository.shouldThrowError = true
        repository.errorMessage = errorMessage

        viewModel.loadStudents()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertTrue(state.students.isEmpty())
    }

    @Test
    fun `loadStudents with empty result sets empty list`() = runTest {
        repository.students = emptyList()

        viewModel.loadStudents()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.students.isEmpty())
    }

    private class FakeStudentRepository : StudentRepository {
        var students = emptyList<Student>()
        var shouldThrowError = false
        var errorMessage = "Unknown error"

        override suspend fun getStudents(): List<Student> {
            if (shouldThrowError) {
                throw Exception(errorMessage)
            }
            return students
        }
    }
}
