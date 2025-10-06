package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.dto.CreateProjectRequest
import com.kseniiashinkar.hackathon_platform.dto.ProjectResponse
import com.kseniiashinkar.hackathon_platform.service.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/projects")
class ProjectController(private val projectService: ProjectService) {

    // Получить все проекты
    @GetMapping
    fun getAllProjects(): ResponseEntity<List<ProjectResponse>> {
        val projects = projectService.findAll()
        val projectResponses = projects.map { ProjectResponse.fromEntity(it) }
        return ResponseEntity.ok(projectResponses)
    }

    // Получить проект по ID
    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: Long): ResponseEntity<ProjectResponse> {
        val project = projectService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ProjectResponse.fromEntity(project))
    }

    // Создать проект
    @PostMapping
    fun createProject(@RequestBody request: CreateProjectRequest): ResponseEntity<Any> {
        try {
            val project = projectService.save(request)
            return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponse.fromEntity(project))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // Обновить проект
    @PutMapping("/{id}")
    fun updateProject(
        @PathVariable id: Long,
        @RequestBody request: CreateProjectRequest
    ): ResponseEntity<Any> {
        try {
            val project = projectService.update(id, request)
            return ResponseEntity.ok(ProjectResponse.fromEntity(project))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // Удалить проект
    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: Long): ResponseEntity<Void> {
        projectService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // Получить проекты команды
    @GetMapping("/teams/{teamId}")
    fun getProjectsByTeam(@PathVariable teamId: Long): ResponseEntity<List<ProjectResponse>> {
        val projects = projectService.findByTeamId(teamId)
        val projectResponses = projects.map { ProjectResponse.fromEntity(it) }
        return ResponseEntity.ok(projectResponses)
    }

}