package com.kseniiashinkar.hackathon_platform.service

import com.kseniiashinkar.hackathon_platform.entity.Team
import com.kseniiashinkar.hackathon_platform.repository.TeamRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamMemberRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class TeamServiceImpl(
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository
) : TeamService {

    // Получить все команды
    override fun findAll(): List<Team> = teamRepository.findAll()
    // Найти команду по ID
    override fun findById(id: Long): Team? = teamRepository.findById(id).orElse(null)

    // Сохранить команду
    override fun save(team: Team): Team {
        // Бизнес-логика: проверка уникальности имени в рамках мероприятия
        if (teamRepository.existsByNameAndEventId(team.name, team.event.id)) {
            throw IllegalArgumentException("Команда с названием '${team.name}' уже существует в этом мероприятии")
        }

        // Бизнес-логика: генерация инвайт-кода если не указан
        if (team.inviteCode.isBlank()) {
            team.inviteCode = generateInviteCode()
        }

        return teamRepository.save(team)
    }

    // Удалить команду
    override fun deleteById(id: Long) {
        val team = findById(id)
        // Бизнес-логика: нельзя удалить команду с участниками
        if (team?.members?.isNotEmpty() == true) {
            throw IllegalStateException("Нельзя удалить команду с участниками")
        }
        teamRepository.deleteById(id)
    }

    // Найти команды по мероприятию
    override fun findByEventId(eventId: Long): List<Team> = teamRepository.findByEventId(eventId)

    // Найти команду по инвайт-коду
    override fun findByInviteCode(inviteCode: String): Team? = teamRepository.findByInviteCode(inviteCode)

    // Поиск команд по названию
    override fun searchByName(name: String): List<Team> = teamRepository.findByNameContainingIgnoreCase(name)

    // Проверить есть ли место в команде
    override fun hasAvailableSlots(teamId: Long): Boolean {
        val team = findById(teamId) ?: return false
        val currentMembers = teamMemberRepository.countByTeamId(teamId)
        return currentMembers < team.maxSize
    }

    // Сгенерировать уникальный инвайт-код
    private fun generateInviteCode(): String {
        val code = UUID.randomUUID().toString().substring(0, 8).uppercase()
        // Проверяем что код уникален
        return if (teamRepository.findByInviteCode(code) == null) code else generateInviteCode()
    }

    // Получить количество участников в команде
    override fun getMemberCount(teamId: Long): Int = teamMemberRepository.countByTeamId(teamId)
}