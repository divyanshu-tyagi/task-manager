package org.example.task_manager.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.task_manager.api.dto.PagedResponse
import org.example.task_manager.api.dto.TaskRequest
import org.example.task_manager.api.dto.TaskResponse
import org.example.task_manager.domain.Priority
import org.example.task_manager.domain.TaskStatus
import org.example.task_manager.repository.UserRepository
import org.example.task_manager.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task Management endpoints ")
@SecurityRequirement(name = "Bearer Authentication")
class TaskController (
    private val taskService: TaskService
){

    @GetMapping
    @Operation(summary = "Get my tasks",
        description = "Supports pagination, search by title, and filtering by status and/or priority")
    fun getMyTasks(
        @RequestParam (required = false)status: TaskStatus? ,
        @RequestParam (required = false) priority: Priority?,
        @RequestParam(required = false) search: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
        ): PagedResponse<TaskResponse> = taskService.getMyTasks( status, priority, search, page, size)


    @GetMapping("/{id}")
    @Operation(summary = "Get a task by Id")
    fun getTaskById(@PathVariable id: Long): TaskResponse {
        return taskService.getTaskById(id)
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    fun createTask(@Valid @RequestBody request: TaskRequest): ResponseEntity<TaskResponse> {
        val created = taskService.createTask(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a existing task")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody request: TaskRequest
    ): TaskResponse = taskService.updateTask(id, request)

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Unit>{
        taskService.deleteTask(id)
        return ResponseEntity.noContent().build()
    }
}