package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.dto.CreateTeamRequest
import com.kseniiashinkar.hackathon_platform.dto.TeamResponse
import com.kseniiashinkar.hackathon_platform.service.EventServiceImpl
import com.kseniiashinkar.hackathon_platform.service.TeamService
import com.kseniiashinkar.hackathon_platform.service.TeamMemberServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/teams")
class TeamController(
    private val teamService: TeamService,
    private val teamMemberService: TeamMemberServiceImpl,
    private val eventService: EventServiceImpl // Добавляем EventService
) {

    // Получить все команды
    @GetMapping
    fun getAllTeams(): ResponseEntity<List<TeamResponse>> {
        val teams = teamService.findAll()
        val teamResponses = teams.map { team ->
            val memberCount = teamService.getMemberCount(team.id)
            TeamResponse.fromEntity(team, memberCount)
        }
        return ResponseEntity.ok(teamResponses)
    }

    // Получить команду по ID
    @GetMapping("/{id}")
    fun getTeamById(@PathVariable id: Long): ResponseEntity<TeamResponse> {
        val team = teamService.findById(id) ?: return ResponseEntity.notFound().build()
        val memberCount = teamService.getMemberCount(id)
        return ResponseEntity.ok(TeamResponse.fromEntity(team, memberCount))
    }

    // Создать новую команду
    @PostMapping
    fun createTeam(@RequestBody request: CreateTeamRequest): ResponseEntity<Any> {
        // Получаем мероприятие из базы данных
        val event = eventService.findById(request.eventId)
            ?: return ResponseEntity.badRequest().body(mapOf("error" to "Мероприятие не найдено"))

        val team = com.kseniiashinkar.hackathon_platform.entity.Team(
            name = request.name,
            description = request.description,
            maxSize = request.maxSize,
            event = event,
            inviteCode = "" // сгенерируется автоматически в сервисе
        )

        try {
            val savedTeam = teamService.save(team)
            val memberCount = teamService.getMemberCount(savedTeam.id)
            return ResponseEntity.status(HttpStatus.CREATED).body(TeamResponse.fromEntity(savedTeam, memberCount))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // Удалить команду
    @DeleteMapping("/{id}")
    fun deleteTeam(@PathVariable id: Long): ResponseEntity<Any> {
        try {
            teamService.deleteById(id)
            return ResponseEntity.noContent().build()
        } catch (e: IllegalStateException) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // Получить команды по мероприятию
    @GetMapping("/event/{eventId}")
    fun getTeamsByEvent(@PathVariable eventId: Long): ResponseEntity<List<TeamResponse>> {
        val teams = teamService.findByEventId(eventId)
        val teamResponses = teams.map { team ->
            val memberCount = teamService.getMemberCount(team.id)
            TeamResponse.fromEntity(team, memberCount)
        }
        return ResponseEntity.ok(teamResponses)
    }

    // Поиск команд по названию
    @GetMapping("/search")
    fun searchTeams(@RequestParam name: String): ResponseEntity<List<TeamResponse>> {
        val teams = teamService.searchByName(name)
        val teamResponses = teams.map { team ->
            val memberCount = teamService.getMemberCount(team.id)
            TeamResponse.fromEntity(team, memberCount)
        }
        return ResponseEntity.ok(teamResponses)
    }

    // Проверить доступность мест в команде
    @GetMapping("/{id}/available-slots")
    fun getAvailableSlots(@PathVariable id: Long): ResponseEntity<Map<String, Boolean>> {
        val hasSlots = teamService.hasAvailableSlots(id)
        return ResponseEntity.ok(mapOf("hasAvailableSlots" to hasSlots))
    }
}