package com.kseniiashinkar.hackathon_platform.dto

import com.kseniiashinkar.hackathon_platform.entity.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

// DTO для регистрации пользователя с валидацией
data class RegisterUserRequest(
    @field:NotBlank(message = "Email обязателен")
    @field:Email(message = "Некорректный формат email")
    val email: String,

    @field:NotBlank(message = "Пароль обязателен")
    @field:Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    val password: String,

    @field:NotBlank(message = "Имя обязательно")
    val firstName: String,

    @field:NotBlank(message = "Фамилия обязательна")
    val lastName: String,

    val phoneNumber: String? = null,
    val skills: String? = null
)

// DTO для входа пользователя
data class LoginRequest(
    @field:NotBlank(message = "Email обязателен")
    @field:Email(message = "Некорректный формат email")
    val email: String,

    @field:NotBlank(message = "Пароль обязателен")
    val password: String
)

// DTO для ответа аутентификации
data class AuthResponse(
    val token: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
)

// DTO для обновления пользователя
data class UpdateUserRequest(
    @field:NotBlank(message = "Имя обязательно")
    val firstName: String? = null,

    @field:NotBlank(message = "Фамилия обязательна")
    val lastName: String? = null,

    val phoneNumber: String? = null,
    val skills: String? = null
)

// DTO для ответа с пользователем
data class UserResponse(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val skills: String?,
    val role: UserRole,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(user: com.kseniiashinkar.hackathon_platform.entity.User): UserResponse {
            return UserResponse(
                id = user.id,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                phoneNumber = user.phoneNumber,
                skills = user.skills,
                role = user.role,
                createdAt = user.createdAt
            )
        }
    }
}

