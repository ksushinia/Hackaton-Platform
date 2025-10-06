package com.kseniiashinkar.hackathon_platform.entity

import jakarta.persistence.*

@Entity
@Table(name = "teams")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(name = "invite_code", unique = true)
    var inviteCode: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "max_size")
    var maxSize: Int = 5,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    var event: Event,

    @OneToMany(mappedBy = "team", cascade = [CascadeType.ALL])
    var members: MutableList<TeamMember> = mutableListOf(),

    @OneToMany(mappedBy = "team", cascade = [CascadeType.ALL])
    var projects: MutableList<Project> = mutableListOf()
)
{
    constructor(
        name: String,
        inviteCode: String,
        event: Event,
        description: String? = null,
        maxSize: Int = 5
    ) : this(
        id = 0,
        name = name,
        inviteCode = inviteCode,
        description = description,
        maxSize = maxSize,
        event = event
    )
}