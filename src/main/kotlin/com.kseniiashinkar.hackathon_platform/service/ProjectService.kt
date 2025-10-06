package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.dto.CreateProjectRequest
import com.kseniiashinkar.hackathon_platform.entity.Project
import com.kseniiashinkar.hackathon_platform.entity.ProjectStatus

interface ProjectService {
    fun findAll(): List<Project>
    fun findById(id: Long): Project?
    fun save(request: CreateProjectRequest): Project
    fun update(id: Long, request: CreateProjectRequest): Project
    fun deleteById(id: Long)
    fun findByTeamId(teamId: Long): List<Project>
    fun findByStatus(status: ProjectStatus): List<Project>
    fun findByTeamEventId(eventId: Long): List<Project>
    fun searchByName(name: String): List<Project>
}