package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.User
import com.kseniiashinkar.hackathon_platform.entity.UserRole
import com.kseniiashinkar.hackathon_platform.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository) {

    // Получить всех пользователей
    fun findAll(): List<User> = userRepository.findAll()

    // Найти пользователя по ID
    fun findById(id: Long): User? = userRepository.findById(id).orElse(null)

    // Сохранить пользователя
    fun save(user: User): User {
        // Бизнес-логика: проверка email
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("Пользователь с email '${user.email}' уже существует")
        }

        // Бизнес-логика: валидация пароля
        if (user.password.length < 6) {
            throw IllegalArgumentException("Пароль должен содержать минимум 6 символов")
        }

        return userRepository.save(user)
    }

    // Удалить пользователя
    fun deleteById(id: Long) {
        val user = findById(id)
        // Бизнес-логика: нельзя удалить пользователя с активными участиями
        if (user?.participations?.isNotEmpty() == true) {
            throw IllegalStateException("Нельзя удалить пользователя с активными участиями в командах")
        }
        userRepository.deleteById(id)
    }

    // Найти пользователя по email
    fun findByEmail(email: String): User? = userRepository.findByEmail(email).orElse(null)

    // Найти пользователей по роли
    fun findByRole(role: UserRole): List<User> = userRepository.findByRole(role)

    // Поиск пользователей по имени или навыкам
    fun searchUsers(query: String): List<User> {
        val byName = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
        val bySkills = userRepository.findBySkillsContainingIgnoreCase(query)

        // Объединяем результаты и убираем дубликаты
        return (byName + bySkills).distinctBy { it.id }
    }

    // Обновить роль пользователя
    fun updateUserRole(userId: Long, newRole: UserRole): User {
        val user = findById(userId) ?: throw IllegalArgumentException("Пользователь не найдена")
        user.role = newRole
        return userRepository.save(user)
    }

    // Аутентификация пользователя (упрощенная версия)
    fun authenticate(email: String, password: String): User? {
        val user = findByEmail(email)
        return if (user != null && user.password == password) user else null
    }
}