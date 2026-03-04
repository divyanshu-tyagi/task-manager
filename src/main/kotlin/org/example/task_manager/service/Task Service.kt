package org.example.task_manager.service

import org.example.task_manager.api.dto.PagedResponse
import org.example.task_manager.api.dto.TaskRequest
import org.example.task_manager.api.dto.TaskResponse
import org.example.task_manager.domain.Priority
import org.example.task_manager.domain.Task
import org.example.task_manager.domain.TaskStatus
import org.example.task_manager.repository.TaskRepository
import org.example.task_manager.repository.UserRepository
import org.example.task_manager.security.AuthUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val authUtils: AuthUtils
){
    fun getMyTasks(
        status: TaskStatus? = null,
        priority: Priority? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 10
    ): PagedResponse<TaskResponse> {
        val currentUser = authUtils.getCurrentUser()

        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val taskPage = when{
            status != null && priority != null ->
                taskRepository.findByUserIdAndStatusAndPriority(currentUser.id, status, priority, pageable)

            status != null ->
                taskRepository.findByUserIdAndStatus(currentUser.id, status,pageable)

            priority != null ->
                taskRepository.findByUserIdAndPriority(currentUser.id, priority,pageable)

            else -> taskRepository.findByUserId(currentUser.id,pageable)
        }
        return PagedResponse.from( taskPage.map { TaskResponse.from(it) })
    }

    fun getTaskById(id: Long): TaskResponse {
        val currentUser = authUtils.getCurrentUser()
        val task = taskRepository.findById(id)
            .orElseThrow{ NoSuchElementException("Task $id not found") }

        if (task.userId != currentUser.id) {
            throw AccessDeniedException("You don't have access to this task")
        }

        return TaskResponse.from(task)
    }

    fun createTask(request: TaskRequest): TaskResponse {
       val currentUser = authUtils.getCurrentUser()
        val task = Task(
            title = request.title,
            description = request.description,
            status = request.status,
            priority = request.priority,
            dueDate = request.dueDate,
            userId = currentUser.id
        )
        return TaskResponse.from(taskRepository.save(task))
    }

    fun updateTask(id: Long, request: TaskRequest): TaskResponse {
        val currentUser = authUtils.getCurrentUser()
        val existing = taskRepository.findById(id)
            .orElseThrow{ NoSuchElementException("Task $id not found") }

        if(existing.userId != currentUser.id){
            throw AccessDeniedException("You don't have access to this task")
        }

        val updated = existing.copy(
            title = request.title,
            description = request.description,
            status = request.status,
            priority = request.priority,
            dueDate = request.dueDate,
        )
        return TaskResponse.from(taskRepository.save(updated))
    }

    fun deleteTask(id: Long) {
        val currentUser = authUtils.getCurrentUser()
        val task = taskRepository.findById(id)
            .orElseThrow{NoSuchElementException("Task $id not found")}

        if(task.userId != currentUser.id){
            throw AccessDeniedException("You don't have access to this task")
        }

        taskRepository.deleteById(id)
    }
}