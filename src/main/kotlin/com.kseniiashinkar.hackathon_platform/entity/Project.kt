package com.kseniiashinkar.hackathon_platform.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "projects")
class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @Column(name = "github_url")
    var githubUrl: String? = null,

    @Column(name = "demo_url")
    var demoUrl: String? = null,

    @Column(name = "submission_date")
    var submissionDate: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var status: ProjectStatus = ProjectStatus.IN_PROGRESS,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team: Team,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(
        name: String,
        description: String,
        team: Team,
        githubUrl: String? = null,
        demoUrl: String? = null
    ) : this(
        id = 0,
        name = name,
        description = description,
        githubUrl = githubUrl,
        demoUrl = demoUrl,
        team = team
    )
}

enum class ProjectStatus {
    IN_PROGRESS, SUBMITTED, UNDER_REVIEW, WINNER, FINALIST
}