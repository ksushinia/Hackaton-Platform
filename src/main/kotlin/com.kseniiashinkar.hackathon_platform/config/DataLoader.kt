package com.kseniiashinkar.hackathon_platform.config

import com.kseniiashinkar.hackathon_platform.entity.*
import com.kseniiashinkar.hackathon_platform.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DataLoader(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val projectRepository: ProjectRepository
) : CommandLineRunner {

    override fun run(args: Array<String>) {
        // Очистка базы (опционально)
        // projectRepository.deleteAll()
        // teamMemberRepository.deleteAll()
        // teamRepository.deleteAll()
        // userRepository.deleteAll()
        // eventRepository.deleteAll()

        // Проверяем есть ли уже данные
        if (eventRepository.count() == 0L) {
            createTestData()
        }
    }

    private fun createTestData() {
        println("🔄 Загрузка тестовых данных...")

        // 1. Создаем мероприятия
        val hackathon = Event(
            name = "Хакатон для начинающих",
            description = "Первый хакатон для новичков в программировании",
            startDate = LocalDateTime.now().plusDays(7),
            endDate = LocalDateTime.now().plusDays(8),
            location = "Онлайн",
            maxParticipants = 50,
            participantCount = 0  // Добавляем количество участников
        )
        val savedHackathon = eventRepository.save(hackathon)

        val workshop = Event(
            name = "Воркшоп по Spring Boot",
            description = "Практический воркшоп по созданию REST API",
            startDate = LocalDateTime.now().plusDays(14),
            endDate = LocalDateTime.now().plusDays(15),
            location = "IT-парк",
            maxParticipants = 30,
            participantCount = 0  // Добавляем количество участников
        )
        val savedWorkshop = eventRepository.save(workshop)

        // 2. Создаем пользователей
        val admin = User(
            email = "admin@hackathon.com",
            password = "admin123",
            firstName = "Админ",
            lastName = "Системы",
            role = UserRole.ADMIN
        )
        val savedAdmin = userRepository.save(admin)

        val organizer = User(
            email = "organizer@hackathon.com",
            password = "org123",
            firstName = "Иван",
            lastName = "Организаторов",
            phoneNumber = "+79991234567",
            skills = "Project Management, Event Planning",
            role = UserRole.ORGANIZER
        )
        val savedOrganizer = userRepository.save(organizer)

        val participant1 = User(
            email = "alex@mail.com",
            password = "pass123",
            firstName = "Алексей",
            lastName = "Программистов",
            phoneNumber = "+79997654321",
            skills = "Java, Spring Boot, Kotlin",
            role = UserRole.PARTICIPANT
        )
        val savedParticipant1 = userRepository.save(participant1)

        val participant2 = User(
            email = "maria@mail.com",
            password = "pass123",
            firstName = "Мария",
            lastName = "Разработчикова",
            phoneNumber = "+79998887766",
            skills = "Python, Django, React",
            role = UserRole.PARTICIPANT
        )
        val savedParticipant2 = userRepository.save(participant2)

        // 3. Создаем команды
        val team1 = Team(
            name = "Кодеры",
            description = "Команда начинающих разработчиков",
            maxSize = 4,
            event = savedHackathon,
            inviteCode = "TEAM123"
        )
        val savedTeam1 = teamRepository.save(team1)

        // 4. Добавляем участников в команды
        val teamMember1 = TeamMember(
            user = savedParticipant1,
            team = savedTeam1,
            role = TeamRole.LEADER
        )
        teamMemberRepository.save(teamMember1)

        val teamMember2 = TeamMember(
            user = savedParticipant2,
            team = savedTeam1,
            role = TeamRole.MEMBER
        )
        teamMemberRepository.save(teamMember2)

        // 5. Создаем проекты
        val project1 = Project(
            name = "Hackathon Platform",
            description = "Платформа для организации хакатонов на Spring Boot",
            githubUrl = "https://github.com/example/hackathon-platform",
            demoUrl = "https://demo.example.com",
            team = savedTeam1
        )
        projectRepository.save(project1)

        println("✅ Тестовые данные успешно загружены!")
        println("   - Мероприятий: ${eventRepository.count()}")
        println("   - Пользователей: ${userRepository.count()}")
        println("   - Команд: ${teamRepository.count()}")
        println("   - Проектов: ${projectRepository.count()}")
    }
}