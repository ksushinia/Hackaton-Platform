package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.dto.CreateProjectRequest
import com.kseniiashinkar.hackathon_platform.entity.Project
import com.kseniiashinkar.hackathon_platform.entity.ProjectStatus
import com.kseniiashinkar.hackathon_platform.repository.ProjectRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamRepository
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val teamRepository: TeamRepository
) : ProjectService {

    override fun findAll(): List<Project> = projectRepository.findAll()

    override fun findById(id: Long): Project? = projectRepository.findById(id).orElse(null)

    override fun save(request: CreateProjectRequest): Project {
        val team = teamRepository.findById(request.teamId)
            .orElseThrow { IllegalArgumentException("Команда не найдена") }

        val project = Project(
            name = request.name,
            description = request.description,
            githubUrl = request.githubUrl,
            demoUrl = request.demoUrl,
            team = team
        )

        return projectRepository.save(project)
    }

    override fun update(id: Long, request: CreateProjectRequest): Project {
        val existingProject = findById(id)
            ?: throw IllegalArgumentException("Проект не найден")

        val team = teamRepository.findById(request.teamId)
            .orElseThrow { IllegalArgumentException("Команда не найдена") }

        existingProject.name = request.name
        existingProject.description = request.description
        existingProject.githubUrl = request.githubUrl
        existingProject.demoUrl = request.demoUrl
        existingProject.team = team

        return projectRepository.save(existingProject)
    }

    override fun deleteById(id: Long) {
        projectRepository.deleteById(id)
    }

    override fun findByTeamId(teamId: Long): List<Project> = projectRepository.findByTeamId(teamId)

    override fun findByStatus(status: ProjectStatus): List<Project> = projectRepository.findByStatus(status)

    override fun findByTeamEventId(eventId: Long): List<Project> = projectRepository.findByTeamEventId(eventId)

    override fun searchByName(name: String): List<Project> = projectRepository.findByNameContainingIgnoreCase(name)
}