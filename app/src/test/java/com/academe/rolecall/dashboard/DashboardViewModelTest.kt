package com.academe.rolecall.dashboard

import com.academe.rolecall.data.models.Attendance
import com.academe.rolecall.data.models.Student
import com.academe.rolecall.data.repository.AttendanceRepository
import com.academe.rolecall.data.repository.StudentRepository
import com.academe.rolecall.data.service.AttendanceService
import com.academe.rolecall.data.service.AuthService
import com.academe.rolecall.data.service.StudentService
import com.academe.rolecall.data.preferences.UserPreferences
import com.academe.rolecall.data.repository.AppSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var studentRepository: FakeStudentRepository
    private lateinit var attendanceService: AttendanceService
    private lateinit var authService: AuthService
    private lateinit var studentService: StudentService
    private lateinit var preferences: UserPreferences

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        preferences = FakeUserPreferences()
        studentRepository = FakeStudentRepository()
        studentService = StudentService(studentRepository)
        
        attendanceService = AttendanceService(FakeAttendanceRepository())
        authService = AuthService(preferences, FakeAppSessionRepository())

        viewModel = DashboardViewModel(studentService, attendanceService, authService, preferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadStudents failure sets error state`() = runTest {
        val errorMessage = "Network Error"
        studentRepository.shouldThrowError = true
        studentRepository.errorMessage = errorMessage

        viewModel.loadStudents()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertTrue(state.students.isEmpty())
    }

    @Test
    fun `loadStudents with empty result sets empty list`() = runTest {
        studentRepository.studentsFlow = flowOf(emptyList())

        viewModel.loadStudents()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.students.isEmpty())
    }

    private class FakeStudentRepository : StudentRepository {
        var studentsFlow: Flow<List<Student>> = flowOf(emptyList())
        var shouldThrowError = false
        var errorMessage = "Unknown error"

        override fun getStudents(): Flow<List<Student>> {
            if (shouldThrowError) {
                return flow { throw Exception(errorMessage) }
            }
            return studentsFlow
        }

        override suspend fun deleteStudent(student: Student) {}
    }

    private class FakeUserPreferences : UserPreferences {
        override val demoModeFlow: Flow<Boolean> = flowOf(false)
        override val authenticatedFlow: Flow<Boolean> = flowOf(false)
        override suspend fun setDemoMode(enabled: Boolean) {}
        override suspend fun setAuthenticated(enabled: Boolean) {}
        override suspend fun clear() {}
    }

    private class FakeAttendanceRepository : AttendanceRepository {
        override fun getAttendanceForStudent(studentId: Long) = flowOf(emptyList<Attendance>())
        override fun getAttendanceForDate(date: String) = flowOf(emptyList<Attendance>())
        override fun getAllAttendanceDates() = flowOf(emptyList<String>())
        override suspend fun insert(attendance: Attendance) {}
        override suspend fun deleteAttendance(studentId: Long, date: String) {}
    }

    private class FakeAppSessionRepository : AppSessionRepository {
        override suspend fun insert(session: com.academe.rolecall.data.models.AppSession) {}
        override suspend fun delete(session: com.academe.rolecall.data.models.AppSession) {}
        override suspend fun getById(id: Long) = null
        override fun getAll() = flowOf(emptyList<com.academe.rolecall.data.models.AppSession>())
        override suspend fun deleteAll() {}
    }
}
