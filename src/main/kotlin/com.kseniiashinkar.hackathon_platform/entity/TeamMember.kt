package com.kseniiashinkar.hackathon_platform.entity

import jakarta.persistence.*

@Entity
@Table(name = "team_members")
class TeamMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team: Team,

    @Enumerated(EnumType.STRING)
    var role: TeamRole = TeamRole.MEMBER,

    @Column(name = "joined_at")
    var joinedAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
) {
    constructor(
        user: User,
        team: Team,
        role: TeamRole = TeamRole.MEMBER
    ) : this(
        id = 0,
        user = user,
        team = team,
        role = role
    )
}

enum class TeamRole {
    LEADER, MEMBER, MENTOR
}