package com.kseniiashinkar.hackathon_platform.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)  // ← Убедись что nullable = false
    var password: String,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(name = "phone_number")
    var phoneNumber: String? = null,

    @Column(name = "skills")
    var skills: String? = null,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.PARTICIPANT,

    @Column(name = "created_at")
    var createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var participations: MutableList<TeamMember> = mutableListOf()
) {
    // Конструктор без id для удобства
    constructor(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String? = null,
        skills: String? = null,
        role: UserRole = UserRole.PARTICIPANT
    ) : this(
        id = 0,
        email = email,
        password = password,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        skills = skills,
        role = role
    )
}

enum class UserRole {
    PARTICIPANT, ORGANIZER, JUDGE, ADMIN
}