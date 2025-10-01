package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.Event
import com.kseniiashinkar.hackathon_platform.entity.EventStatus

interface EventService {
    fun findAll(): List<Event>
    fun findById(id: Long): Event?
    fun save(event: Event): Event
    fun deleteById(id: Long)
    fun findByStatus(status: EventStatus): List<Event>
    fun findUpcomingEvents(): List<Event>
    fun searchByName(name: String): List<Event>
    fun updateEventStatus(eventId: Long, newStatus: EventStatus): Event
    fun isRegistrationOpen(eventId: Long): Boolean
    fun countParticipants(eventId: Long): Int
}