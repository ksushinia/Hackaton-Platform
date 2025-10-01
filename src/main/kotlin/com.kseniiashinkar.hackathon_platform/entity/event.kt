package com.kseniiashinkar.hackathon_platform.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    var endDate: LocalDateTime,

    @Column(name = "location")
    var location: String? = null,

    @Column(name = "max_participants")
    var maxParticipants: Int = 100,

    @Enumerated(EnumType.STRING)
    var status: EventStatus = EventStatus.UPCOMING,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL])
    var teams: MutableList<Team> = mutableListOf()
) {
    // Конструктор без id для удобства создания
    constructor(
        name: String,
        description: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        location: String? = null,
        maxParticipants: Int = 100
    ) : this(
        id = 0,
        name = name,
        description = description,
        startDate = startDate,
        endDate = endDate,
        location = location,
        maxParticipants = maxParticipants
    )
}

enum class EventStatus {
    UPCOMING, ONGOING, COMPLETED, CANCELLED
}