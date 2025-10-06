package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.TeamMember
import com.kseniiashinkar.hackathon_platform.entity.TeamRole
import com.kseniiashinkar.hackathon_platform.repository.TeamMemberRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamRepository
import com.kseniiashinkar.hackathon_platform.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class TeamMemberServiceImpl(
    private val teamMemberRepository: TeamMemberRepository,
    private val teamRepository: TeamRepository,
    private val userRepository: UserRepository
) : TeamMemberService {  // ← ВАЖНО: добавить имплементацию интерфейса

    override fun addMemberToTeam(userId: Long, teamId: Long, role: TeamRole): TeamMember {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("Пользователь не найден") }
        val team = teamRepository.findById(teamId).orElseThrow { IllegalArgumentException("Команда не найдена") }

        if (teamMemberRepository.existsByUserIdAndTeamId(userId, teamId)) {
            throw IllegalStateException("Пользователь уже состоит в этой команде")
        }

        val currentMembers = teamMemberRepository.countByTeamId(teamId)
        if (currentMembers >= team.maxSize) {
            throw IllegalStateException("В команде нет свободных мест")
        }

        val teamMember = TeamMember(user = user, team = team, role = role)
        return teamMemberRepository.save(teamMember)
    }

    override fun removeMemberFromTeam(userId: Long, teamId: Long) {
        val teamMember = teamMemberRepository.findByUserIdAndTeamId(userId, teamId)
            ?: throw IllegalArgumentException("Участник не найден в команде")

        if (teamMember.role == TeamRole.LEADER) {
            val otherMembers = teamMemberRepository.findByTeamId(teamId).filter { it.user.id != userId }
            if (otherMembers.isNotEmpty()) {
                throw IllegalStateException("Нельзя удалить лидера пока в команде есть другие участники")
            }
        }

        teamMemberRepository.delete(teamMember)
    }

    override fun findMembersByTeam(teamId: Long): List<TeamMember> = teamMemberRepository.findByTeamId(teamId)

    override fun findTeamsByUser(userId: Long): List<TeamMember> = teamMemberRepository.findByUserId(userId)

    override fun updateMemberRole(userId: Long, teamId: Long, newRole: TeamRole): TeamMember {
        val teamMember = teamMemberRepository.findByUserIdAndTeamId(userId, teamId)
            ?: throw IllegalArgumentException("Участник не найден")

        teamMember.role = newRole
        return teamMemberRepository.save(teamMember)
    }

    override fun isUserInTeam(userId: Long, teamId: Long): Boolean =
        teamMemberRepository.existsByUserIdAndTeamId(userId, teamId)
}