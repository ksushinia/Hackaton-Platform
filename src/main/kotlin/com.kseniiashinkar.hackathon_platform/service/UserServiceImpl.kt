package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.dto.RegisterUserRequest
import com.kseniiashinkar.hackathon_platform.entity.User
import com.kseniiashinkar.hackathon_platform.entity.UserRole
import com.kseniiashinkar.hackathon_platform.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder // Добавляем шифрование паролей
) : UserService {

    override fun findAll(): List<User> = userRepository.findAll()

    override fun findById(id: Long): User? = userRepository.findById(id).orElse(null)

    override fun save(user: User): User {
        // Бизнес-логика: проверка email
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("Пользователь с email '${user.email}' уже существует")
        }

        // ШИФРУЕМ пароль перед сохранением!
        user.password = passwordEncoder.encode(user.password)

        return userRepository.save(user)
    }

    override fun deleteById(id: Long) {
        val user = findById(id)
        if (user?.participations?.isNotEmpty() == true) {
            throw IllegalStateException("Нельзя удалить пользователя с активными участиями в командах")
        }
        userRepository.deleteById(id)
    }

    override fun findByEmail(email: String): User? = userRepository.findByEmail(email).orElse(null)

    override fun findByRole(role: UserRole): List<User> = userRepository.findByRole(role)

    override fun searchUsers(query: String): List<User> {
        val byName = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
        val bySkills = userRepository.findBySkillsContainingIgnoreCase(query)

        return (byName + bySkills).distinctBy { it.id }
    }

    override fun updateUserRole(userId: Long, newRole: UserRole): User {
        val user = findById(userId) ?: throw IllegalArgumentException("Пользователь не найдена")
        user.role = newRole
        return userRepository.save(user)
    }

    override fun authenticate(email: String, password: String): User? {
        val user = findByEmail(email)
        // Теперь проверяем пароль через passwordEncoder
        return if (user != null && passwordEncoder.matches(password, user.password)) user else null
    }
    override fun registerNewUser(request: RegisterUserRequest): User {
        // 1. Проверяем, существует ли уже пользователь с таким email
        if (existsByEmail(request.email)) {
            throw IllegalArgumentException("Пользователь с email '${request.email}' уже существует")
        }

        // 2. Создаем сущность User на основе данных из DTO
        val newUser = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            // 3. СРАЗУ ШИФРУЕМ ПАРОЛЬ ПЕРЕД СОХРАНЕНИЕМ
            password = passwordEncoder.encode(request.password),
            role = UserRole.PARTICIPANT // По умолчанию присваиваем роль USER
        )

        // 4. Сохраняем нового пользователя в базу
        return userRepository.save(newUser)
    }

    // --- ДОБАВЛЯЕМ НОВЫЙ МЕТОД ДЛЯ ПРОВЕРКИ EMAIL ---
    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}