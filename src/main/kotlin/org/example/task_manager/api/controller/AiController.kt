package org.example.task_manager.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.task_manager.api.dto.PrioritySuggestionRequest
import org.example.task_manager.api.dto.PrioritySuggestionResponse
import org.example.task_manager.api.dto.TaskEnhancementResponse
import org.example.task_manager.domain.Priority
import org.example.task_manager.repository.TaskRepository
import org.example.task_manager.security.AuthUtils
import org.example.task_manager.service.GeminiService
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Features", description = "Gemini AI powered endpoints")
@SecurityRequirement(name = "Bearer Authentication")
class AiController(
    private val geminiService: GeminiService,
    private val taskRepository: TaskRepository,
    private val authUtils: AuthUtils
    ) {
    @PostMapping("/tasks/{id}/enhance")
    @Operation(
        summary = "Enhance task description",
        description = "Uses Gemini AI to generate a clear, detailed description for your task"
    )
    fun enhanceTask(@PathVariable id: Long): TaskEnhancementResponse {
        val currentUser = authUtils.getCurrentUser()

        val task = taskRepository.findById(id)
            .orElseThrow { NoSuchElementException("Task $id not found") }

        if (task.userId != currentUser.id) {
            throw AccessDeniedException("You don't have access to this task")
        }

        val enhanced = geminiService.enhanceTask(task.title, task.description)

        return TaskEnhancementResponse(
            taskId              = task.id,
            title               = task.title,
            enhancedDescription = enhanced
        )
    }

    // POST /api/ai/suggest-priority
    @PostMapping("/suggest-priority")
    @Operation(
        summary = "Suggest task priority",
        description = "Uses Gemini AI to suggest HIGH, MEDIUM, or LOW priority based on task title"
    )
    fun suggestPriority(@RequestBody request: PrioritySuggestionRequest): PrioritySuggestionResponse {
        val suggested = geminiService.suggestPriority(request.title)

        val priority = runCatching {
            Priority.valueOf(suggested)
        }.getOrDefault(Priority.MEDIUM)

        return PrioritySuggestionResponse(
            title             = request.title,
            suggestedPriority = priority,
            message           = "AI suggested priority based on task title"
        )
    }
}