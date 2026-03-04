package org.example.task_manager.api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.example.task_manager.domain.Priority
import org.example.task_manager.domain.TaskStatus
import java.time.LocalDate

data class TaskRequest(

    @field:NotBlank(message = "Title must not be blank")
    val title: String,

    val description: String? = null,

    val status: TaskStatus = TaskStatus.TODO,

    val priority: Priority = Priority.MEDIUM,

    val dueDate: LocalDate? = null,

)