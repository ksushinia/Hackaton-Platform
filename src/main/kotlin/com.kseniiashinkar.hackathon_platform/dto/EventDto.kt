package com.kseniiashinkar.hackathon_platform.dto

import com.kseniiashinkar.hackathon_platform.entity.EventStatus
import java.time.LocalDateTime

// DTO для создания мероприятия
data class CreateEventRequest(
    val name: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val location: String? = null,
    val maxParticipants: Int = 100
)

// DTO для обновления мероприятия
data class UpdateEventRequest(
    val name: String? = null,
    val description: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val location: String? = null,
    val maxParticipants: Int? = null,
    val status: EventStatus? = null
)

// DTO для ответа с мероприятием
data class EventResponse(
    val id: Long,
    val name: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val location: String?,
    val maxParticipants: Int,
    val status: EventStatus,
    val participantCount: Int,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(event: com.kseniiashinkar.hackathon_platform.entity.Event, participantCount: Int = 0): EventResponse {
            return EventResponse(
                id = event.id,
                name = event.name,
                description = event.description,
                startDate = event.startDate,
                endDate = event.endDate,
                location = event.location,
                maxParticipants = event.maxParticipants,
                status = event.status,
                participantCount = participantCount,
                createdAt = event.createdAt
            )
        }
    }
}