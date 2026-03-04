package org.example.task_manager.api.dto

import org.example.task_manager.domain.User
import java.time.Instant

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: Instant
){
    companion object {
        fun from(user: User) = UserResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            createdAt = user.createdAt
        )
    }
}
