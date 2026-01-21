package de.dkb.api.codeChallenge.notification.controller

import de.dkb.api.codeChallenge.notification.model.NotificationDto
import de.dkb.api.codeChallenge.notification.model.NotificationTypeCategory
import de.dkb.api.codeChallenge.notification.service.NotificationService
import de.dkb.api.codeChallenge.user.model.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(private val notificationService: NotificationService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody user: User) =
        notificationService.registerUser(user)

    @PostMapping("/notify")
    fun sendNotification(@RequestBody notificationDto: NotificationDto) =
        notificationService.sendNotification(notificationDto)

    @PostMapping("/addType")
    fun registerUser(@RequestBody notificationType: NotificationTypeCategory) =
        notificationService.addNotificationType(notificationType)
}