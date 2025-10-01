package com.kseniiashinkar.hackathon_platform.dto

import com.kseniiashinkar.hackathon_platform.entity.TeamRole

// DTO для вступления в команду
data class JoinTeamRequest(
    val inviteCode: String,
    val role: TeamRole = TeamRole.MEMBER
)

// DTO для ответа с участником команды
data class TeamMemberResponse(
    val id: Long,
    val userId: Long,
    val userName: String,
    val userEmail: String,
    val teamId: Long,
    val teamName: String,
    val role: TeamRole,
    val joinedAt: java.time.LocalDateTime
) {
    companion object {
        fun fromEntity(teamMember: com.kseniiashinkar.hackathon_platform.entity.TeamMember): TeamMemberResponse {
            return TeamMemberResponse(
                id = teamMember.id,
                userId = teamMember.user.id,
                userName = "${teamMember.user.firstName} ${teamMember.user.lastName}",
                userEmail = teamMember.user.email,
                teamId = teamMember.team.id,
                teamName = teamMember.team.name,
                role = teamMember.role,
                joinedAt = teamMember.joinedAt
            )
        }
    }
}