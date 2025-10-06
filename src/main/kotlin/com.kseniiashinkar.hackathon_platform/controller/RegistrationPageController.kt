// в файле src/main/kotlin/.../controller/RegistrationPageController.kt
package com.kseniiashinkar.hackathon_platform.controller

import com.kseniiashinkar.hackathon_platform.dto.RegisterUserRequest // <-- Используем ваш DTO
import com.kseniiashinkar.hackathon_platform.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class RegistrationPageController(
    private val userService: UserService
) {

    // Этот метод просто показывает страницу регистрации (остается без изменений)
    @GetMapping("/register")
    fun showRegistrationPage(): String {
        return "register"
    }

    // Этот метод обрабатывает данные из формы
    @PostMapping("/register")
    fun registerUser(
        // Принимаем те же параметры из формы
        @RequestParam firstName: String,
        @RequestParam lastName: String,
        @RequestParam email: String,
        @RequestParam password: String,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            // Создаем DTO из данных формы
            val registrationRequest = RegisterUserRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password
            )
            // Вызываем единый метод в сервисе, который делает всю работу
            userService.registerNewUser(registrationRequest)

        } catch (e: IllegalArgumentException) {
            // Ловим конкретную ошибку, если email уже занят
            redirectAttributes.addFlashAttribute("error", e.message)
            return "redirect:/register"
        } catch (e: Exception) {
            // Ловим все остальные возможные ошибки
            redirectAttributes.addFlashAttribute("error", "Произошла непредвиденная ошибка при регистрации.")
            return "redirect:/register"
        }

        redirectAttributes.addFlashAttribute("success", "Регистрация прошла успешно! Теперь вы можете войти в систему.")
        return "redirect:/login"
    }
}