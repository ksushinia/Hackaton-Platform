// в файле src/main/kotlin/.../controller/ProjectPageController.kt
package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.entity.Project
import com.kseniiashinkar.hackathon_platform.repository.ProjectRepository
import com.kseniiashinkar.hackathon_platform.repository.TeamRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller // <-- ВАЖНО: именно @Controller, а не @RestController
class ProjectPageController(
    private val projectRepository: ProjectRepository,
    private val teamRepository: TeamRepository
) {

    /**
     * Этот метод обрабатывает данные, отправленные из HTML-формы на странице /projects.
     * Он не использует JSON и предназначен только для работы с веб-интерфейсом.
     */
    @PostMapping("/projects/create")
    fun createProjectFromForm(
        @RequestParam teamId: Long,
        @RequestParam projectName: String,
        @RequestParam projectDescription: String,
        redirectAttributes: RedirectAttributes
    ): String { // <-- Возвращает String для перенаправления
        val team = teamRepository.findById(teamId).orElse(null)

        // Проверяем, существует ли команда
        if (team == null) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: выбранная команда не найдена!")
            return "redirect:/projects"
        }

        val project = Project(
            name = projectName,
            description = projectDescription,
            team = team
        )
        projectRepository.save(project)

        // Добавляем "flash" атрибут, который будет доступен после перенаправления
        redirectAttributes.addFlashAttribute("success", "Проект '${project.name}' был успешно создан!")

        // Перенаправляем пользователя обратно на страницу проектов
        return "redirect:/projects"
    }
}