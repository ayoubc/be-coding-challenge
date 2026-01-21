package de.dkb.api.codeChallenge

import de.dkb.api.codeChallenge.notification.model.NotificationDto
import de.dkb.api.codeChallenge.notification.model.NotificationTypeCategory
import de.dkb.api.codeChallenge.notification.repository.NotificationTypesRepository
import de.dkb.api.codeChallenge.notification.service.NotificationService
import de.dkb.api.codeChallenge.user.model.User
import de.dkb.api.codeChallenge.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID

@Testcontainers
@SpringBootTest
class CodeChallengeApplicationTests @Autowired constructor(
	private val service: NotificationService,
	private val userRepository: UserRepository,
	private val notificationTypesRepository: NotificationTypesRepository,
) {
	companion object {
		@Container
		val postgres = PostgreSQLContainer("postgres:15")

		@JvmStatic
		@DynamicPropertySource
		@Suppress("unused", "UsePropertyAccessSyntax")
		fun registerPgProperties(registry: org.springframework.test.context.DynamicPropertyRegistry) {
			registry.add("spring.datasource.url", postgres::getJdbcUrl)
			registry.add("spring.datasource.username", postgres::getUsername)
			registry.add("spring.datasource.password", postgres::getPassword)
		}
	}

	init {
	    postgres.start()
	}

	@Test
	fun contextLoads() {
	}

	@Test
	fun `sends when categories overlap across different types`() {
		val type1 = notificationTypesRepository.save(NotificationTypeCategory("type1", "CatA"))
		val type2 = notificationTypesRepository.save(NotificationTypeCategory("type2", "CatA"))
		val user = userRepository.save(User(id = UUID.randomUUID(), notifications = mutableSetOf(type1.name)))

		val dto = NotificationDto(userId = user.id, notificationType = type2.name, message = "hello world")
		val result = service.sendNotification(dto)

		assertTrue(result.startsWith("Sending notification of type ${type2.name}"))
		assertTrue(result.contains(user.id.toString()))
		assertTrue(result.contains("hello world"))
	}

	@Test
	fun `returns generic message when categories do not overlap`() {
		val typeX = notificationTypesRepository.save(NotificationTypeCategory("typeX", "CatX"))
		val typeY = notificationTypesRepository.save(NotificationTypeCategory("typeY", "CatY"))
		val user = userRepository.save(User(id = UUID.randomUUID(), notifications = mutableSetOf(typeX.name)))

		val dto = NotificationDto(userId = user.id, notificationType = typeY.name, message = "failed")
		val result = service.sendNotification(dto)

		assertEquals("Could not send notification", result)
	}
}
