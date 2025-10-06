package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.entity.Event
import com.kseniiashinkar.hackathon_platform.repository.EventRepository
import com.kseniiashinkar.hackathon_platform.repository.ProjectRepository
import com.kseniiashinkar.hackathon_platform.repository.UserRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.data.domain.Pageable // <-- Импортируем Pageable
import org.springframework.data.web.PageableDefault // <-- Импортируем аннотацию для настроек по умолчанию
import java.time.format.DateTimeFormatter

@Controller
class PageController(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository, // Добавлен
    private val teamRepository: TeamRepository, // Добавлен
    private val projectRepository: ProjectRepository
) {

    @GetMapping("/")
    fun homePage(model: Model): String {
        model.addAttribute("title", "Hackathon Platform")

        // Передаем реальную статистику
        model.addAttribute("totalEvents", eventRepository.count())
        model.addAttribute("totalUsers", userRepository.count()) // Новое
        model.addAttribute("totalProjects", projectRepository.count()) // Новое

        return "home"
    }

    @GetMapping("/events")
    fun eventsPage(model: Model): String {
        val events = eventRepository.findAll()
        model.addAttribute("title", "Хакатоны")
        model.addAttribute("events", events)
        return "events"
    }

    @GetMapping("/events/{id}")
    fun eventDetailsPage(@PathVariable id: Long, model: Model): String {
        val event = eventRepository.findById(id).orElse(null)
        model.addAttribute("title", "Детали хакатона")
        model.addAttribute("event", event)
        return "event-details"
    }

    // остальные методы без изменений...
    @GetMapping("/login")
    fun loginPage(model: Model): String {
        model.addAttribute("title", "Вход в систему")
        return "login"
    }

    @GetMapping("/profile")
    fun profilePage(model: Model, authentication: Authentication): String {
        // Получаем email текущего аутентифицированного пользователя
        val email = authentication.name

        // Находим пользователя в базе данных
        val userOptional = userRepository.findByEmail(email)

        if (userOptional.isEmpty) {
            // Если пользователь не найден (маловероятно, но возможно),
            // перенаправляем на страницу входа
            return "redirect:/login"
        }
        val user = userOptional.get()

        // Используем новый метод для поиска всех команд пользователя
        val userTeams = teamRepository.findByMembers_User(user)

        // Получаем уникальный список событий, в которых пользователь участвует,
        // на основе его команд
        val userEvents = userTeams.map { it.event }.distinct()

        // Передаем все данные в модель для отображения в шаблоне
        model.addAttribute("user", user)
        model.addAttribute("userTeams", userTeams)
        model.addAttribute("userEvents", userEvents)
        model.addAttribute("title", "Мой профиль")

        return "profile" // Возвращаем имя шаблона profile.html
    }

    @GetMapping("/teams")
    fun teamsPage(
        model: Model,
        authentication: Authentication,
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable // <-- НОВОЕ: Добавляем пагинацию
    ): String {
        val email = authentication.name
        val user = userRepository.findByEmail(email).orElse(null)

        if (user == null) {
            return "redirect:/login"
        }

        // --- Логика для "Моих команд" остается без изменений ---
        val myTeams = teamRepository.findByMembers_User(user)
        model.addAttribute("myTeams", myTeams)

        // --- НОВАЯ ЛОГИКА для "Всех команд" ---
        // JpaRepository уже умеет работать с Pageable, нам не нужно писать новый метод
        val allTeamsPage = teamRepository.findAll(pageable)
        model.addAttribute("allTeamsPage", allTeamsPage)

        model.addAttribute("title", "Управление командами")

        return "teams"
    }

    @GetMapping("/projects")
    fun projectsPage(model: Model, authentication: Authentication): String {
        val email = authentication.name
        val user = userRepository.findByEmail(email).orElse(null)

        if (user == null) {
            return "redirect:/login"
        }

        // 1. Находим команды пользователя
        val myTeams = teamRepository.findByMembers_User(user)

        // 2. Находим проекты этих команд
        val myProjects = if (myTeams.isNotEmpty()) {
            projectRepository.findByTeamIn(myTeams)
        } else {
            emptyList() // Если команд нет, то и проектов нет
        }

        model.addAttribute("myProjects", myProjects)
        model.addAttribute("myTeams", myTeams) // Передаем команды для формы создания проекта
        model.addAttribute("title", "Мои проекты")

        return "projects"
    }
}