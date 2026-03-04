package org.example.task_manager.security

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank(message = "Name cannot be blank")
    val name: String,

    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Must be a valid email")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    val password: String
)

data class LoginRequest(
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    val password: String
)

data class AuthResponse(
    val token: String,
    val email: String,
    val name: String
)
