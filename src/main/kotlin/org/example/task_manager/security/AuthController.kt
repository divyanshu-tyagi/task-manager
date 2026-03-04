package org.example.task_manager.security

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.task_manager.domain.User
import org.example.task_manager.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Register and Login endpoints")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        // Check email not already taken
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email ${request.email} is already taken")
        }

        val user = userRepository.save(
            User(
                name = request.name,
                email = request.email,
                password = passwordEncoder.encode(request.password)
            )
        )

        val token = jwtService.generateToken(user.email)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(AuthResponse(token = token, email = user.email, name = user.name))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): AuthResponse {
        // This validates email + password — throws if wrong
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val user = userRepository.findByEmail(request.email)!!
        val token = jwtService.generateToken(user.email)
        return AuthResponse(token = token, email = user.email, name = user.name)
    }
}
