package org.example.task_manager.service


import org.example.task_manager.domain.Priority
import org.example.task_manager.domain.Task
import org.example.task_manager.domain.TaskStatus
import org.example.task_manager.domain.User
import org.example.task_manager.repository.TaskRepository
import org.example.task_manager.security.AuthUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.access.AccessDeniedException
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional

class TaskServiceTest {

    private val taskRepository: TaskRepository = mock()
    private val authUtils: AuthUtils = mock()
    private lateinit var taskService: TaskService

    private val currentUser = User(id = 1L, name = "TestUser", email = "testUser@gmail.com")

    private val sampleTask = Task(
        id = 1L,
        title = "Fix login bug",
        status = TaskStatus.TODO,
        priority = Priority.HIGH,
        userId = 1L
    )

    @BeforeEach
    fun setup() {
        taskService = TaskService(taskRepository, authUtils)
        whenever(authUtils.getCurrentUser()).thenReturn(currentUser)
    }

    @Test
    fun `getMyTasks returns tasks for current user`() {
        val page = PageImpl(listOf(sampleTask))
        whenever(taskRepository.findByUserId(any(), any<Pageable>())).thenReturn(page)

        val result = taskService.getMyTasks()

        assertEquals(1, result.totalElements)
        assertEquals("Fix login bug", result.content[0].title)
    }

    @Test
    fun `getTaskById throws when task not found`() {
        whenever(taskRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            taskService.getTaskById(99L)
        }
    }

    @Test
    fun `getTaskById throws when task belongs to different user`() {
        val otherUsersTask = sampleTask.copy(userId = 2L)
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(otherUsersTask))

        assertThrows<AccessDeniedException> {
            taskService.getTaskById(1L)
        }
    }

    @Test
    fun `deleteTask throws when task belongs to different user`() {
        val otherUsersTask = sampleTask.copy(userId = 2L)
        whenever(taskRepository.findById(1L)).thenReturn(Optional.of(otherUsersTask))

        assertThrows<AccessDeniedException> {
            taskService.deleteTask(1L)
        }
    }
}