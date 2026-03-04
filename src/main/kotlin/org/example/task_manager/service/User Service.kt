package org.example.task_manager.service

import org.example.task_manager.api.dto.UserRequest
import org.example.task_manager.api.dto.UserResponse
import org.example.task_manager.domain.User
import org.example.task_manager.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository
){
    fun getAllUsers() : List<UserResponse> =
        userRepository.findAll().map { UserResponse.from(it) }

    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow{ NoSuchElementException("User $id not found") }
        return UserResponse.from(user)
    }

    fun deleteUser(id: Long){
        if (!userRepository.existsById(id)){
            throw NoSuchElementException("User $id not found")
        }
        userRepository.deleteById(id)
    }
}