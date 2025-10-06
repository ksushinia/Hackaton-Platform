package com.kseniiashinkar.hackathon_platform.dto

import com.kseniiashinkar.hackathon_platform.entity.EventStatus
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.LocalDateTime

// DTO для создания мероприятия с валидацией
data class CreateEventRequest(
    @field:NotBlank(message = "Название мероприятия обязательно")
    val name: String,

    @field:NotBlank(message = "Описание обязательно")
    val description: String,

    @field:NotNull(message = "Дата начала обязательна")
    @field:Future(message = "Дата начала должна быть в будущем")
    val startDate: LocalDateTime,

    @field:NotNull(message = "Дата окончания обязательна")
    @field:Future(message = "Дата окончания должна быть в будущем")
    val endDate: LocalDateTime,

    val location: String? = null,

    @field:Positive(message = "Количество участников должно быть положительным")
    val maxParticipants: Int = 100
) {
    init {
        if (startDate.isAfter(endDate)) {
            throw IllegalArgumentException("Дата начала не может быть позже даты окончания")
        }
    }
}

// DTO для обновления мероприятия
data class UpdateEventRequest(
    @field:NotBlank(message = "Название мероприятия обязательно")
    val name: String? = null,

    @field:NotBlank(message = "Описание обязательно")
    val description: String? = null,

    @field:Future(message = "Дата начала должна быть в будущем")
    val startDate: LocalDateTime? = null,

    @field:Future(message = "Дата окончания должна быть в будущем")
    val endDate: LocalDateTime? = null,

    val location: String? = null,

    @field:Positive(message = "Количество участников должно быть положительным")
    val maxParticipants: Int? = null,

    val status: EventStatus? = null
)

// DTO для ответа с мероприятием (остается без изменений)
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