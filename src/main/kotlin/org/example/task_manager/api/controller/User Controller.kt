package org.example.task_manager.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.task_manager.api.dto.UserRequest
import org.example.task_manager.api.dto.UserResponse
import org.example.task_manager.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User Management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    @Operation(summary = "Get all users")
    fun getAllUsers(): List<UserResponse> = userService.getAllUsers()

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by Id")
    fun getUserById(@PathVariable id: Long): UserResponse = userService.getUserById(id)

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by Id")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}