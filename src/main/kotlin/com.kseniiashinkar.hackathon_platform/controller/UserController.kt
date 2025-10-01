package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.dto.*
import com.kseniiashinkar.hackathon_platform.service.UserServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserServiceImpl) {

    // Получить всех пользователей
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userService.findAll()
        val userResponses = users.map { UserResponse.fromEntity(it) }
        return ResponseEntity.ok(userResponses)
    }

    // Получить пользователя по ID
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserResponse.fromEntity(user))
    }

    // Зарегистрировать нового пользователя
    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterUserRequest): ResponseEntity<UserResponse> {
        val user = com.kseniiashinkar.hackathon_platform.entity.User(
            email = request.email,
            password = request.password, // В реальном приложении нужно хэшировать!
            firstName = request.firstName,
            lastName = request.lastName,
            phoneNumber = request.phoneNumber,
            skills = request.skills
        )

        val savedUser = userService.save(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromEntity(savedUser))
    }

    // Вход пользователя
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<UserResponse> {
        val user = userService.authenticate(request.email, request.password)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return ResponseEntity.ok(UserResponse.fromEntity(user))
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val existingUser = userService.findById(id) ?: return ResponseEntity.notFound().build()

        // Обновляем только переданные поля
        request.firstName?.let { existingUser.firstName = it }
        request.lastName?.let { existingUser.lastName = it }
        request.phoneNumber?.let { existingUser.phoneNumber = it }
        request.skills?.let { existingUser.skills = it }

        val updatedUser = userService.save(existingUser)
        return ResponseEntity.ok(UserResponse.fromEntity(updatedUser))
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // Поиск пользователей
    @GetMapping("/search")
    fun searchUsers(@RequestParam query: String): ResponseEntity<List<UserResponse>> {
        val users = userService.searchUsers(query)
        val userResponses = users.map { UserResponse.fromEntity(it) }
        return ResponseEntity.ok(userResponses)
    }

    // Получить пользователя по email
    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<UserResponse> {
        val user = userService.findByEmail(email) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserResponse.fromEntity(user))
    }
}