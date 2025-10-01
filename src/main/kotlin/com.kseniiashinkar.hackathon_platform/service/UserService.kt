package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.User
import com.kseniiashinkar.hackathon_platform.entity.UserRole

interface UserService {
    fun findAll(): List<User>
    fun findById(id: Long): User?
    fun save(user: User): User
    fun deleteById(id: Long)
    fun findByEmail(email: String): User?
    fun findByRole(role: UserRole): List<User>
    fun searchUsers(query: String): List<User>
    fun updateUserRole(userId: Long, newRole: UserRole): User
    fun authenticate(email: String, password: String): User?
}