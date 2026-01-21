package de.dkb.api.codeChallenge.notification.controller

import de.dkb.api.codeChallenge.notification.model.NotificationDto
import de.dkb.api.codeChallenge.notification.service.NotificationService
import de.dkb.api.codeChallenge.notification.repository.NotificationTypesRepository
import de.dkb.api.codeChallenge.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.Optional
import java.util.UUID

class NotificationControllerTest {

    private lateinit var userRepository: UserRepository
    private lateinit var notificationTypesRepository: NotificationTypesRepository
    private lateinit var service: NotificationService
    private lateinit var controller: NotificationController

    @BeforeEach
    fun setUp() {
        userRepository = Mockito.mock(UserRepository::class.java)
        notificationTypesRepository = Mockito.mock(NotificationTypesRepository::class.java)
        service = NotificationService(userRepository, notificationTypesRepository)
        controller = NotificationController(service)
    }

    @Test
    fun `sendNotification returns value from service`() {
        val userId = UUID.randomUUID()
        val dto = NotificationDto(userId = userId, notificationType = "type1", message = "hello")

        `when`(notificationTypesRepository.findAll()).thenReturn(emptyList())
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        val result = controller.sendNotification(dto)

        assertEquals("Could not send notification", result)
    }
}
