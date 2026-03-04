package org.example.task_manager.security

import org.example.task_manager.domain.User
import org.example.task_manager.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthUtils(
    private val userRepository: UserRepository,
) {
    fun getCurrentUser(): User{
        val email = SecurityContextHolder.getContext().authentication?.name
            ?:throw RuntimeException("No authenticated user found")
        return userRepository.findByEmail(email)
            ?: throw RuntimeException("Authenticated user not found in DB")
    }
}