package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.dto.JoinTeamRequest
import com.kseniiashinkar.hackathon_platform.dto.TeamMemberResponse
import com.kseniiashinkar.hackathon_platform.service.TeamMemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/team-members")
class TeamMemberController(private val teamMemberService: TeamMemberService) {

    // Вступить в команду
    @PostMapping("/join")
    fun joinTeam(@RequestBody request: JoinTeamRequest): ResponseEntity<Any> {
        try {
            val teamMember = teamMemberService.addMemberToTeam(
                userId = request.userId, // нужно будет получить из аутентификации
                teamId = request.teamId,
                role = request.role
            )
            return ResponseEntity.status(HttpStatus.CREATED).body(TeamMemberResponse.fromEntity(teamMember))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // Покинуть команду
    @DeleteMapping("/teams/{teamId}/users/{userId}")
    fun leaveTeam(
        @PathVariable teamId: Long,
        @PathVariable userId: Long
    ): ResponseEntity<Any> {
        try {
            teamMemberService.removeMemberFromTeam(userId, teamId)
            return ResponseEntity.noContent().build()
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // Получить участников команды
    @GetMapping("/teams/{teamId}")
    fun getTeamMembers(@PathVariable teamId: Long): ResponseEntity<List<TeamMemberResponse>> {
        val members = teamMemberService.findMembersByTeam(teamId)
        val memberResponses = members.map { TeamMemberResponse.fromEntity(it) }
        return ResponseEntity.ok(memberResponses)
    }

    // Получить команды пользователя
    @GetMapping("/users/{userId}")
    fun getUserTeams(@PathVariable userId: Long): ResponseEntity<List<TeamMemberResponse>> {
        val members = teamMemberService.findTeamsByUser(userId)
        val memberResponses = members.map { TeamMemberResponse.fromEntity(it) }
        return ResponseEntity.ok(memberResponses)
    }
}