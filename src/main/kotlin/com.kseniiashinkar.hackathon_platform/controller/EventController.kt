package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.dto.CreateEventRequest
import com.kseniiashinkar.hackathon_platform.dto.EventResponse
import com.kseniiashinkar.hackathon_platform.dto.UpdateEventRequest
import com.kseniiashinkar.hackathon_platform.entity.EventStatus
import com.kseniiashinkar.hackathon_platform.service.EventServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventServiceImpl) {

    // Получить все мероприятия
    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventResponse>> {
        val events = eventService.findAll()
        val eventResponses = events.map { event ->
            val participantCount = eventService.countParticipants(event.id)
            EventResponse.fromEntity(event, participantCount)
        }
        return ResponseEntity.ok(eventResponses)
    }

    // Получить мероприятие по ID
    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long): ResponseEntity<EventResponse> {
        val event = eventService.findById(id) ?: return ResponseEntity.notFound().build()
        val participantCount = eventService.countParticipants(id)
        return ResponseEntity.ok(EventResponse.fromEntity(event, participantCount))
    }

    // Создать новое мероприятие
    @PostMapping
    fun createEvent(@RequestBody request: CreateEventRequest): ResponseEntity<EventResponse> {
        val event = com.kseniiashinkar.hackathon_platform.entity.Event(
            name = request.name,
            description = request.description,
            startDate = request.startDate,
            endDate = request.endDate,
            location = request.location,
            maxParticipants = request.maxParticipants
        )

        val savedEvent = eventService.save(event)
        return ResponseEntity.status(HttpStatus.CREATED).body(EventResponse.fromEntity(savedEvent))
    }

    // Обновить мероприятие
    @PutMapping("/{id}")
    fun updateEvent(
        @PathVariable id: Long,
        @RequestBody request: UpdateEventRequest
    ): ResponseEntity<EventResponse> {
        val existingEvent = eventService.findById(id) ?: return ResponseEntity.notFound().build()

        // Обновляем только переданные поля
        request.name?.let { existingEvent.name = it }
        request.description?.let { existingEvent.description = it }
        request.startDate?.let { existingEvent.startDate = it }
        request.endDate?.let { existingEvent.endDate = it }
        request.location?.let { existingEvent.location = it }
        request.maxParticipants?.let { existingEvent.maxParticipants = it }
        request.status?.let { existingEvent.status = it }

        val updatedEvent = eventService.save(existingEvent)
        val participantCount = eventService.countParticipants(id)
        return ResponseEntity.ok(EventResponse.fromEntity(updatedEvent, participantCount))
    }

    // Удалить мероприятие
    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: Long): ResponseEntity<Void> {
        eventService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // Получить мероприятия по статусу
    @GetMapping("/status/{status}")
    fun getEventsByStatus(@PathVariable status: EventStatus): ResponseEntity<List<EventResponse>> {
        val events = eventService.findByStatus(status)
        val eventResponses = events.map { event ->
            val participantCount = eventService.countParticipants(event.id)
            EventResponse.fromEntity(event, participantCount)
        }
        return ResponseEntity.ok(eventResponses)
    }

    // Поиск мероприятий по названию
    @GetMapping("/search")
    fun searchEvents(@RequestParam name: String): ResponseEntity<List<EventResponse>> {
        val events = eventService.searchByName(name)
        val eventResponses = events.map { event ->
            val participantCount = eventService.countParticipants(event.id)
            EventResponse.fromEntity(event, participantCount)
        }
        return ResponseEntity.ok(eventResponses)
    }

    // Получить предстоящие мероприятия
    @GetMapping("/upcoming")
    fun getUpcomingEvents(): ResponseEntity<List<EventResponse>> {
        val events = eventService.findUpcomingEvents()
        val eventResponses = events.map { event ->
            val participantCount = eventService.countParticipants(event.id)
            EventResponse.fromEntity(event, participantCount)
        }
        return ResponseEntity.ok(eventResponses)
    }

    // Проверить доступность регистрации
    @GetMapping("/{id}/registration-status")
    fun getRegistrationStatus(@PathVariable id: Long): ResponseEntity<Map<String, Boolean>> {
        val isOpen = eventService.isRegistrationOpen(id)
        return ResponseEntity.ok(mapOf("registrationOpen" to isOpen))
    }
}