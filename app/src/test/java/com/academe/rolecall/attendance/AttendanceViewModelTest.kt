package com.academe.rolecall.attendance

import com.academe.rolecall.data.models.Attendance
import com.academe.rolecall.data.models.Student
import com.academe.rolecall.data.repository.AttendanceRepository
import com.academe.rolecall.data.repository.StudentRepository
import com.academe.rolecall.data.service.AttendanceService
import com.academe.rolecall.data.service.StudentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
class AttendanceViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AttendanceViewModel
    private lateinit var attendanceRepository: FakeAttendanceRepository
    private lateinit var studentRepository: FakeStudentRepository
    private lateinit var attendanceService: AttendanceService
    private lateinit var studentService: StudentService

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        attendanceRepository = FakeAttendanceRepository()
        studentRepository = FakeStudentRepository()
        attendanceService = AttendanceService(attendanceRepository)
        studentService = StudentService(studentRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDates failure does not crash and keeps dates empty`() = runTest {
        attendanceRepository.shouldThrowError = true
        
        viewModel = AttendanceViewModel(attendanceService, studentService)

        assertTrue(viewModel.state.value.dates.isEmpty())
        assertNull(viewModel.state.value.selectedDate)
    }

    @Test
    fun `loadStudents failure does not crash and keeps students empty`() = runTest {
        studentRepository.shouldThrowError = true
        
        viewModel = AttendanceViewModel(attendanceService, studentService)

        assertTrue(viewModel.state.value.students.isEmpty())
    }

    @Test
    fun `selectDate failure sets loading to false and keeps list empty`() = runTest {
        val date = "2023-10-27"
        attendanceRepository.shouldThrowError = true
        
        viewModel = AttendanceViewModel(attendanceService, studentService)
        viewModel.selectDate(date)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.attendanceList.isEmpty())
        assertEquals(date, state.selectedDate)
    }

    private class FakeAttendanceRepository : AttendanceRepository {
        var shouldThrowError = false

        override fun getAttendanceForStudent(studentId: Long): Flow<List<Attendance>> = flow {
            if (shouldThrowError) throw Exception("Error")
            emit(emptyList())
        }

        override fun getAttendanceForDate(date: String): Flow<List<Attendance>> = flow {
            if (shouldThrowError) throw Exception("Error")
            emit(emptyList())
        }

        override fun getAllAttendanceDates(): Flow<List<String>> = flow {
            if (shouldThrowError) throw Exception("Error")
            emit(emptyList())
        }

        override suspend fun insert(attendance: Attendance) {}
        override suspend fun deleteAttendance(studentId: Long, date: String) {}
    }

    private class FakeStudentRepository : StudentRepository {
        var shouldThrowError = false

        override fun getStudents(): Flow<List<Student>> = flow {
            if (shouldThrowError) throw Exception("Error")
            emit(emptyList())
        }

        override suspend fun deleteStudent(student: Student) {}
    }
}
