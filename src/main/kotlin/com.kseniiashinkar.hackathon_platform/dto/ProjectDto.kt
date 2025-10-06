package com.kseniiashinkar.hackathon_platform.dto

import com.kseniiashinkar.hackathon_platform.entity.ProjectStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

// DTO для создания проекта
data class CreateProjectRequest(
    @field:NotBlank(message = "Название проекта обязательно")
    val name: String,

    @field:NotBlank(message = "Описание проекта обязательно")
    val description: String,

    val githubUrl: String? = null,
    val demoUrl: String? = null,

    @field:NotNull(message = "ID команды обязателен")
    val teamId: Long
)

// DTO для ответа с проектом
data class ProjectResponse(
    val id: Long,
    val name: String,
    val description: String,
    val githubUrl: String?,
    val demoUrl: String?,
    val submissionDate: LocalDateTime?,
    val status: ProjectStatus,
    val teamId: Long,
    val teamName: String,
    val eventName: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(project: com.kseniiashinkar.hackathon_platform.entity.Project): ProjectResponse {
            return ProjectResponse(
                id = project.id,
                name = project.name,
                description = project.description,
                githubUrl = project.githubUrl,
                demoUrl = project.demoUrl,
                submissionDate = project.submissionDate,
                status = project.status,
                teamId = project.team.id,
                teamName = project.team.name,
                eventName = project.team.event.name,
                createdAt = project.createdAt
            )
        }
    }
}