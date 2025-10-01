package com.kseniiashinkar.hackathon_platform.dto

import com.kseniiashinkar.hackathon_platform.entity.UserRole
import java.time.LocalDateTime

// DTO для регистрации пользователя
data class RegisterUserRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null,
    val skills: String? = null
)

// DTO для входа пользователя
data class LoginRequest(
    val email: String,
    val password: String
)

// DTO для обновления пользователя
data class UpdateUserRequest(
    val firstName: String? = null,
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