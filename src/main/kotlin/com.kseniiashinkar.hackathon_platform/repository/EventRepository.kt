package com.kseniiashinkar.hackathon_platform.repository

import com.kseniiashinkar.hackathon_platform.entity.Event
import com.kseniiashinkar.hackathon_platform.entity.EventStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    // Найти мероприятия по статусу
    fun findByStatus(status: EventStatus): List<Event>

    // Найти предстоящие мероприятия (дата начала в будущем)
    fun findByStartDateAfter(currentDate: LocalDateTime): List<Event>

    // Найти мероприятия по названию (поиск)
    fun findByNameContainingIgnoreCase(name: String): List<Event>

    // Найти мероприятия в определенном временном диапазоне
    @Query("SELECT e FROM Event e WHERE e.startDate BETWEEN :start AND :end")
    fun findEventsBetweenDates(
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime
    ): List<Event>

    // Проверить существует ли мероприятие с таким названием
    fun existsByName(name: String): Boolean
}