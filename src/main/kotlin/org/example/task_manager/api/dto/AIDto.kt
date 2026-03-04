package org.example.task_manager.api.dto

import org.example.task_manager.domain.Priority

data class PrioritySuggestionResponse(
    val title: String,
    val suggestedPriority: Priority,
    val message: String
)

data class TaskEnhancementResponse (
    val taskId: Long,
    val title: String,
    val enhancedDescription: String
)

data class PrioritySuggestionRequest(
    val title: String,
)