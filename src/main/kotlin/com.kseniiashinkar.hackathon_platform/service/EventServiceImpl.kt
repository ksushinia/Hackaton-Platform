package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.Event
import com.kseniiashinkar.hackathon_platform.entity.EventStatus
import com.kseniiashinkar.hackathon_platform.repository.EventRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EventServiceImpl(private val eventRepository: EventRepository) {

    // Получить все мероприятия
    fun findAll(): List<Event> = eventRepository.findAll()

    // Найти мероприятие по ID
    fun findById(id: Long): Event? = eventRepository.findById(id).orElse(null)

    // Сохранить мероприятие
    fun save(event: Event): Event {
        // Бизнес-логика: проверка дат
        if (event.startDate.isAfter(event.endDate)) {
            throw IllegalArgumentException("Дата начала не может быть позже даты окончания")
        }

        // Бизнес-логика: проверка названия
        if (eventRepository.existsByName(event.name)) {
            throw IllegalArgumentException("Мероприятие с названием '${event.name}' уже существует")
        }

        return eventRepository.save(event)
    }

    // Удалить мероприятие
    fun deleteById(id: Long) {
        val event = findById(id)
        // Бизнес-логика: нельзя удалить активное мероприятие
        if (event?.status == EventStatus.ONGOING) {
            throw IllegalStateException("Нельзя удалить мероприятие в процессе проведения")
        }
        eventRepository.deleteById(id)
    }

    // Найти мероприятия по статусу
    fun findByStatus(status: EventStatus): List<Event> = eventRepository.findByStatus(status)

    // Найти предстоящие мероприятия
    fun findUpcomingEvents(): List<Event> = eventRepository.findByStartDateAfter(LocalDateTime.now())

    // Найти мероприятия по названию
    fun searchByName(name: String): List<Event> = eventRepository.findByNameContainingIgnoreCase(name)

    // Обновить статус мероприятия
    fun updateEventStatus(eventId: Long, newStatus: EventStatus): Event {
        val event = findById(eventId) ?: throw IllegalArgumentException("Мероприятие не найдено")

        // Бизнес-логика: валидация перехода статусов
        when {
            event.status == EventStatus.COMPLETED && newStatus != EventStatus.COMPLETED ->
                throw IllegalStateException("Завершенное мероприятие нельзя изменить")
            event.status == EventStatus.CANCELLED && newStatus != EventStatus.CANCELLED ->
                throw IllegalStateException("Отмененное мероприятие нельзя изменить")
        }

        event.status = newStatus
        return eventRepository.save(event)
    }

    // Проверить доступность регистрации
    fun isRegistrationOpen(eventId: Long): Boolean {
        val event = findById(eventId) ?: return false
        return event.status == EventStatus.UPCOMING &&
                event.startDate.isAfter(LocalDateTime.now()) &&
                (event.maxParticipants == 0 || countParticipants(eventId) < event.maxParticipants)
    }

    // Посчитать количество участников мероприятия
    fun countParticipants(eventId: Long): Int {
        val event = findById(eventId) ?: return 0
        return event.teams.sumOf { it.members.size }
    }
}