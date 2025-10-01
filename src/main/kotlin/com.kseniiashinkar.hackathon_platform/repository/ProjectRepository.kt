package com.kseniiashinkar.hackathon_platform.repository

import com.kseniiashinkar.hackathon_platform.entity.Project
import com.kseniiashinkar.hackathon_platform.entity.ProjectStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {

    // Найти проекты по команде
    fun findByTeamId(teamId: Long): List<Project>

    // Найти проекты по статусу
    fun findByStatus(status: ProjectStatus): List<Project>

    // Найти проекты по мероприятию
    fun findByTeamEventId(eventId: Long): List<Project>

    // Найти проекты по названию (поиск)
    fun findByNameContainingIgnoreCase(name: String): List<Project>
}