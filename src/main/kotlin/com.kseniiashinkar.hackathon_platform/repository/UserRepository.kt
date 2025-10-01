package com.kseniiashinkar.hackathon_platform.repository

import com.kseniiashinkar.hackathon_platform.entity.User
import com.kseniiashinkar.hackathon_platform.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    // Найти пользователя по email
    fun findByEmail(email: String): Optional<User>

    // Проверить существует ли пользователь с таким email
    fun existsByEmail(email: String): Boolean

    // Найти пользователей по роли
    fun findByRole(role: UserRole): List<User>

    // Найти пользователей по имени или фамилии
    fun findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        firstName: String,
        lastName: String
    ): List<User>

    // Найти пользователей по навыкам
    fun findBySkillsContainingIgnoreCase(skills: String): List<User>
}