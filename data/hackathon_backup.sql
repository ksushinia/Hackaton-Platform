-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: hackathon_platform
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `end_date` datetime(6) NOT NULL,
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_participants` int DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_date` datetime(6) NOT NULL,
  `status` enum('CANCELLED','COMPLETED','ONGOING','UPCOMING') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `participant_count` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES (1,'2025-10-01 10:23:30.589377','Первый хакатон для новичков в программировании','2025-10-09 10:23:30.589377','Онлайн',50,'Хакатон для начинающих','2025-10-08 10:23:30.589377','UPCOMING',0),(2,'2025-10-01 10:23:30.652349','Практический воркшоп по созданию REST API','2025-10-16 10:23:30.652349','IT-парк',30,'Воркшоп по Spring Boot','2025-10-15 10:23:30.652349','UPCOMING',0),(3,'2025-10-04 10:01:19.905642','Соревнование по созданию AI-решений для реальных проблем','2025-11-12 15:00:00.000000','Технопарк',100,'Хакатон по ИИ и Машинному обучению','2025-11-10 06:00:00.000000','UPCOMING',0);
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projects` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `demo_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `github_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('FINALIST','IN_PROGRESS','SUBMITTED','UNDER_REVIEW','WINNER') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `submission_date` datetime(6) DEFAULT NULL,
  `team_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmqih0928bq6r3gbuh47giq8w` (`team_id`),
  CONSTRAINT `FKmqih0928bq6r3gbuh47giq8w` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects`
--

LOCK TABLES `projects` WRITE;
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
INSERT INTO `projects` VALUES (1,'2025-10-01 10:23:30.747210','https://demo.example.com','Платформа для организации хакатонов на Spring Boot','https://github.com/example/hackathon-platform','Hackathon Platform','IN_PROGRESS',NULL,1);
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_members`
--

DROP TABLE IF EXISTS `team_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `joined_at` datetime(6) DEFAULT NULL,
  `role` enum('LEADER','MEMBER','MENTOR') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `team_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtgca08el3ofisywcf11f0f76t` (`team_id`),
  KEY `FKee8x7x5026imwmma9kndkxs36` (`user_id`),
  CONSTRAINT `FKee8x7x5026imwmma9kndkxs36` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKtgca08el3ofisywcf11f0f76t` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_members`
--

LOCK TABLES `team_members` WRITE;
/*!40000 ALTER TABLE `team_members` DISABLE KEYS */;
INSERT INTO `team_members` VALUES (1,'2025-10-01 10:23:30.721212','LEADER',1,3),(2,'2025-10-01 10:23:30.736267','MEMBER',1,4),(3,'2025-10-04 10:30:43.252226','MEMBER',1,10),(4,'2025-10-04 13:33:30.814775','LEADER',4,7);
/*!40000 ALTER TABLE `team_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` text COLLATE utf8mb4_unicode_ci,
  `invite_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_size` int DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9in08syd61oyydey3f63ai6x9` (`invite_code`),
  KEY `FKsjcq76oa92bm6gjbebp2spajm` (`event_id`),
  CONSTRAINT `FKsjcq76oa92bm6gjbebp2spajm` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (1,'Команда начинающих разработчиков','TEAM123',4,'Кодеры',1),(2,'Команда специалистов по искусственному интеллекту','22E495DC',5,'AI Innovators',1),(3,'Команда full-stack разработчиков','EE1FE132',5,'Web Masters',1),(4,'Личная команда участника','',5,'Команда Security Test',2);
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` enum('ADMIN','JUDGE','ORGANIZER','PARTICIPANT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `skills` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2025-10-01 10:23:30.664740','admin@hackathon.com','Админ','Системы','admin123',NULL,'ADMIN',NULL),(2,'2025-10-01 10:23:30.676106','organizer@hackathon.com','Иван','Организаторов','org123','+79991234567','ORGANIZER','Project Management, Event Planning'),(3,'2025-10-01 10:23:30.686592','alex@mail.com','Алексей','Программистов','pass123','+79997654321','PARTICIPANT','Java, Spring Boot, Kotlin'),(4,'2025-10-01 10:23:30.696798','maria@mail.com','Мария','Разработчикова','pass123','+79998887766','PARTICIPANT','Python, Django, React'),(6,'2025-10-01 19:41:23.964188','new_participant@mail.com','Дмитрий','Разработчиков','newpassword123','+79998887766','PARTICIPANT','JavaScript, React, Node.js'),(7,'2025-10-02 19:17:35.225348','security_test@mail.com','Security','Test','$2a$10$v7aKzdl9S3txKuvDhWU2QeUGBGFh.zPdlGtkX9RpV0lRGwsYHRLwm',NULL,'PARTICIPANT',NULL),(8,'2025-10-04 10:11:54.594964','backend_dev@mail.com','Михаил','Бэкендов','$2a$10$3y4DUSnh01SVp1fe0HC6eudF87H5HHEam/FZUi737ySQKo7gas4Im',NULL,'PARTICIPANT','Java, Spring Boot, PostgreSQL'),(9,'2025-10-04 10:13:10.101629','frontend_dev@mail.com','Виктория','Фронтендова','$2a$10$nkh.cYV6mP4O70JJL/CYxOy37zX9Mcs8jMuNOc4zpUvquTje1s956',NULL,'PARTICIPANT','React, TypeScript, CSS'),(10,'2025-10-04 10:20:55.185726','data_scientist@mail.com','Дмитрий','Датасаинтистов','$2a$10$3MSlK8py5HV2xZanpw6PhujgrD8fOkVtUY3VYPQT2vxTnsLoCdSnW','+79526693561','PARTICIPANT','Python, Pandas, Machine Learning');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-04 22:14:41
