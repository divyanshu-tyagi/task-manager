package org.example.task_manager.api.dto

import org.example.task_manager.domain.Priority
import org.example.task_manager.domain.Task
import org.example.task_manager.domain.TaskStatus
import java.time.Instant
import java.time.LocalDate

data class TaskResponse(

    val id: Long,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: Priority,
    val dueDate: LocalDate?,
    val userId: Long,
    val createdAt: Instant
) {
companion object {
    fun from(task: Task)= TaskResponse(
            id = task.id,
            title = task.title,
            description = task.description,
            status = task.status,
            priority = task.priority,
            dueDate = task.dueDate,
            userId = task.userId,
            createdAt = task.createdAt
        )
    }
}