package com.kseniiashinkar.hackathon_platform.repository

import com.kseniiashinkar.hackathon_platform.entity.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : JpaRepository<Team, Long> {

    // Найти команды по мероприятию
    fun findByEventId(eventId: Long): List<Team>

    // Найти команду по инвайт-коду
    fun findByInviteCode(inviteCode: String): Team?

    // Проверить существует ли команда с таким именем в мероприятии
    fun existsByNameAndEventId(name: String, eventId: Long): Boolean

    // Найти команды по названию (поиск)
    fun findByNameContainingIgnoreCase(name: String): List<Team>

    // Найти команды с количеством участников
    @Query("""
        SELECT t, COUNT(tm) as memberCount 
        FROM Team t LEFT JOIN t.members tm 
        WHERE t.event.id = :eventId 
        GROUP BY t
    """)
    fun findTeamsWithMemberCountByEventId(@Param("eventId") eventId: Long): List<Array<Any>>
}