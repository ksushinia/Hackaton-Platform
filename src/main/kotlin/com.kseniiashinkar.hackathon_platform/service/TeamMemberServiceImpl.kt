package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.TeamMember
import com.kseniiashinkar.hackathon_platform.repository.TeamMemberRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamRepository
import com.kseniiashinkar.hackathon_platform.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class TeamMemberServiceImpl(
    private val teamMemberRepository: TeamMemberRepository,
    private val teamRepository: TeamRepository,
    private val userRepository: UserRepository
) {

    // Добавить участника в команду
    fun addMemberToTeam(userId: Long, teamId: Long, role: com.kseniiashinkar.hackathon_platform.entity.TeamRole): TeamMember {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("Пользователь не найден") }
        val team = teamRepository.findById(teamId).orElseThrow { IllegalArgumentException("Команда не найдена") }

        // Бизнес-логика: проверка что пользователь уже не в команде
        if (teamMemberRepository.existsByUserIdAndTeamId(userId, teamId)) {
            throw IllegalStateException("Пользователь уже состоит в этой команде")
        }

        // Бизнес-логика: проверка что есть свободные места
        val currentMembers = teamMemberRepository.countByTeamId(teamId)
        if (currentMembers >= team.maxSize) {
            throw IllegalStateException("В команде нет свободных мест")
        }

        val teamMember = TeamMember(user = user, team = team, role = role)
        return teamMemberRepository.save(teamMember)
    }

    // Удалить участника из команды
    fun removeMemberFromTeam(userId: Long, teamId: Long) {
        val teamMember = teamMemberRepository.findByUserIdAndTeamId(userId, teamId)
            ?: throw IllegalArgumentException("Участник не найден в команде")

        // Бизнес-логика: нельзя удалить лидера если в команде есть другие участники
        if (teamMember.role == com.kseniiashinkar.hackathon_platform.entity.TeamRole.LEADER) {
            val otherMembers = teamMemberRepository.findByTeamId(teamId).filter { it.user.id != userId }
            if (otherMembers.isNotEmpty()) {
                throw IllegalStateException("Нельзя удалить лидера пока в команде есть другие участники")
            }
        }

        teamMemberRepository.delete(teamMember)
    }

    // Найти участников команды
    fun findMembersByTeam(teamId: Long): List<TeamMember> = teamMemberRepository.findByTeamId(teamId)

    // Найти команды пользователя
    fun findTeamsByUser(userId: Long): List<TeamMember> = teamMemberRepository.findByUserId(userId)

    // Обновить роль участника
    fun updateMemberRole(userId: Long, teamId: Long, newRole: com.kseniiashinkar.hackathon_platform.entity.TeamRole): TeamMember {
        val teamMember = teamMemberRepository.findByUserIdAndTeamId(userId, teamId)
            ?: throw IllegalArgumentException("Участник не найден")

        teamMember.role = newRole
        return teamMemberRepository.save(teamMember)
    }

    // Проверить является ли пользователь участником команды
    fun isUserInTeam(userId: Long, teamId: Long): Boolean =
        teamMemberRepository.existsByUserIdAndTeamId(userId, teamId)
}