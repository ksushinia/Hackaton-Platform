package com.kseniiashinkar.hackathon_platform.dto

// DTO для создания команды
data class CreateTeamRequest(
    val name: String,
    val description: String? = null,
    val maxSize: Int = 5,
    val eventId: Long
)

// DTO для ответа с командой
data class TeamResponse(
    val id: Long,
    val name: String,
    val inviteCode: String,
    val description: String?,
    val maxSize: Int,
    val currentSize: Int,
    val eventId: Long,
    val eventName: String
) {
    companion object {
        fun fromEntity(team: com.kseniiashinkar.hackathon_platform.entity.Team, currentSize: Int = 0): TeamResponse {
            return TeamResponse(
                id = team.id,
                name = team.name,
                inviteCode = team.inviteCode,
                description = team.description,
                maxSize = team.maxSize,
                currentSize = currentSize,
                eventId = team.event.id,
                eventName = team.event.name
            )
        }
    }
}