package com.kseniiashinkar.hackathon_platform.repository

import com.kseniiashinkar.hackathon_platform.entity.TeamMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamMemberRepository : JpaRepository<TeamMember, Long> {

    // Найти участников по команде
    fun findByTeamId(teamId: Long): List<TeamMember>

    // Найти участников по пользователю
    fun findByUserId(userId: Long): List<TeamMember>

    // Найти конкретного участника в команде
    fun findByUserIdAndTeamId(userId: Long, teamId: Long): TeamMember?

    // Проверить есть ли пользователь в команде
    fun existsByUserIdAndTeamId(userId: Long, teamId: Long): Boolean

    // Посчитать количество участников в команде
    fun countByTeamId(teamId: Long): Int
}
