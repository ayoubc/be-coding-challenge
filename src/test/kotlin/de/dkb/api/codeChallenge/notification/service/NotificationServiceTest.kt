package de.dkb.api.codeChallenge.notification.service

import de.dkb.api.codeChallenge.notification.model.NotificationDto
import de.dkb.api.codeChallenge.notification.model.NotificationTypeCategory
import de.dkb.api.codeChallenge.notification.repository.NotificationTypesRepository
import de.dkb.api.codeChallenge.user.model.User
import de.dkb.api.codeChallenge.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.Optional
import java.util.UUID

class NotificationServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var notificationTypesRepository: NotificationTypesRepository
    private lateinit var service: NotificationService

    @BeforeEach
    fun setUp() {
        userRepository = Mockito.mock(UserRepository::class.java)
        notificationTypesRepository = Mockito.mock(NotificationTypesRepository::class.java)
        service = NotificationService(userRepository, notificationTypesRepository)
    }

    @Test
    fun `sends notification when user subscribed to same category`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, notifications = mutableSetOf("type1"))
        val dto = NotificationDto(userId = userId, notificationType = "type1", message = "hello")

        `when`(notificationTypesRepository.findAll()).thenReturn(listOf(
            NotificationTypeCategory(name = "type1", category = "catA")
        ))
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val result = service.sendNotification(dto)

        assertTrue(result.contains("Sending notification of type type1"))
        assertTrue(result.contains(userId.toString()))
        assertTrue(result.contains("hello"))
    }

    @Test
    fun `does not send when user subscribed to different category`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, notifications = mutableSetOf("type2"))
        val dto = NotificationDto(userId = userId, notificationType = "type1", message = "hello")

        `when`(notificationTypesRepository.findAll()).thenReturn(listOf(
            NotificationTypeCategory(name = "type1", category = "catA"),
            NotificationTypeCategory(name = "type2", category = "catB"),
        ))
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val result = service.sendNotification(dto)

        assertEquals("Could not send notification", result)
    }

    @Test
    fun `sends when user subscribed to different type but same category`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, notifications = mutableSetOf("typeA1"))
        val dto = NotificationDto(userId = userId, notificationType = "typeA2", message = "msg")

        `when`(notificationTypesRepository.findAll()).thenReturn(listOf(
            NotificationTypeCategory(name = "typeA1", category = "news"),
            NotificationTypeCategory(name = "typeA2", category = "news"),
        ))
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val result = service.sendNotification(dto)

        assertTrue(result.startsWith("Sending notification of type typeA2"))
        assertTrue(result.contains(userId.toString()))
        assertTrue(result.contains("msg"))
    }

    @Test
    fun `does not send when user not found`() {
        val userId = UUID.randomUUID()
        val dto = NotificationDto(userId = userId, notificationType = "type1", message = "hello")

        `when`(notificationTypesRepository.findAll()).thenReturn(listOf(
            NotificationTypeCategory(name = "type1", category = "catA")
        ))
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        val result = service.sendNotification(dto)

        assertEquals("Could not send notification", result)
    }

    @Test
    fun `does not send when no notification types exist`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, notifications = mutableSetOf("type1"))
        val dto = NotificationDto(userId = userId, notificationType = "type1", message = "hello")

        `when`(notificationTypesRepository.findAll()).thenReturn(emptyList())
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val result = service.sendNotification(dto)

        assertEquals("Could not send notification", result)
    }
}
