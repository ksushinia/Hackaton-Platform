package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.entity.Team
import com.kseniiashinkar.hackathon_platform.entity.TeamMember
import com.kseniiashinkar.hackathon_platform.entity.TeamRole
import com.kseniiashinkar.hackathon_platform.repository.EventRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamMemberRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamRepository
import com.kseniiashinkar.hackathon_platform.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class RegistrationController(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository
) {

    @PostMapping("/events/register")
    fun registerForEvent(
        @RequestParam eventId: Long,
        authentication: Authentication,
        redirectAttributes: RedirectAttributes
    ): String {
        val email = authentication.name
        val user = userRepository.findByEmail(email).orElse(null)
        val event = eventRepository.findById(eventId).orElse(null)

        if (user == null || event == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь или событие не найдены")
            return "redirect:/events"
        }

        // Проверяем, не зарегистрирован ли уже пользователь
        val existingTeams = teamRepository.findByEventIdAndUser(eventId, user)
        if (existingTeams.isNotEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Вы уже зарегистрированы на этот хакатон")
            return "redirect:/events/$eventId"
        }

        // Создаем новую команду для пользователя
        val team = Team(
            name = "Команда ${user.firstName} ${user.lastName}",
            description = "Личная команда участника",
            event = event,
            inviteCode = "" // Пустая строка вместо null
        )
        val savedTeam = teamRepository.save(team)

        // Добавляем пользователя в команду как лидера
        val teamMember = TeamMember(
            team = savedTeam,
            user = user,
            role = TeamRole.LEADER // Используем TeamRole enum
        )
        teamMemberRepository.save(teamMember)

        redirectAttributes.addFlashAttribute("success", "Вы успешно зарегистрировались на хакатон!")
        return "redirect:/events/$eventId"
    }

    @GetMapping("/events/teams")
    fun eventTeams(
        @RequestParam eventId: Long,
        model: Model
    ): String {
        val event = eventRepository.findById(eventId).orElse(null)
        val teams = teamRepository.findByEventIdWithMembers(eventId)

        model.addAttribute("event", event)
        model.addAttribute("teams", teams)
        return "event-teams"
    }

    @PostMapping("/teams/create")
    fun createTeam(
        @RequestParam eventId: Long,
        @RequestParam teamName: String,
        @RequestParam teamDescription: String,
        authentication: Authentication,
        redirectAttributes: RedirectAttributes
    ): String {
        val email = authentication.name
        val user = userRepository.findByEmail(email).orElse(null)
        val event = eventRepository.findById(eventId).orElse(null)

        if (user == null || event == null) {
            redirectAttributes.addFlashAttribute("error", "Ошибка создания команды")
            return "redirect:/events/$eventId"
        }

        // Проверяем, не состоит ли пользователь уже в команде на этом хакатоне
        val existingTeams = teamRepository.findByEventIdAndUser(eventId, user)
        if (existingTeams.isNotEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Вы уже состоите в команде на этом хакатоне")
            return "redirect:/events/teams?eventId=$eventId"
        }

        // Создаем новую команду
        val team = Team(
            name = teamName,
            description = teamDescription,
            event = event,
            inviteCode = "" // Пустая строка вместо null
        )
        val savedTeam = teamRepository.save(team)

        // Добавляем создателя как лидера
        val teamMember = TeamMember(
            team = savedTeam,
            user = user,
            role = TeamRole.LEADER // Используем TeamRole enum
        )
        teamMemberRepository.save(teamMember)

        redirectAttributes.addFlashAttribute("success", "Команда создана успешно!")
        return "redirect:/events/teams?eventId=$eventId"
    }
}