// в файле src/main/kotlin/.../config/SecurityConfig.kt

package com.kseniiashinkar.hackathon_platform.config

import com.kseniiashinkar.hackathon_platform.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    // 1. Внедряем ваш сервис для поиска пользователей
    private val userDetailsService: CustomUserDetailsService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        // Указываем Spring Security, как находить пользователей и проверять пароли
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
        return authenticationManagerBuilder.build()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 2. Включаем CSRF для безопасности веб-приложения. Thymeleaf обработает это за вас.
            // .csrf { csrf -> csrf.disable() } // Не отключаем!
            .authorizeHttpRequests { auth ->
                auth
                    // 3. Расширяем список публичных страниц для веб-сайта
                    .requestMatchers(
                        // Публичные страницы сайта
                        "/", "/login", "/events", "/events/{id:\\d+}", "/events/teams", "/register",
                        // Статические ресурсы
                        "/css/**", "/js/**", "/images/**",
                        // Публичные эндпоинты API
                        "/api/users/register",
                        // Служебные страницы
                        "/error",
                        "/h2-console/**"
                    ).permitAll()

                    // 4. Явно указываем, что все остальные запросы требуют аутентификации
                    .anyRequest().authenticated()
            }
            // 5. Настраиваем аутентификацию через форму для веб-сайта
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/login")              // Наша кастомная страница входа
                    .defaultSuccessUrl("/", true) // Куда перенаправить после успешного входа
                    .permitAll()                      // Разрешаем доступ к /login всем
            }
            // 6. Настраиваем выход из системы
            .logout { logout ->
                logout
                    .logoutUrl("/logout")                   // URL для выхода
                    .logoutSuccessUrl("/login?logout")      // Куда перенаправить после выхода
                    .permitAll()
            }

            // 7. Оставляем Basic Auth для работы вашего REST API (например, через Postman)
            .httpBasic { }


        // Для работы с H2-консолью во фрейме
        http.headers { headers -> headers.frameOptions { it.sameOrigin() } }

        return http.build()
    }
}