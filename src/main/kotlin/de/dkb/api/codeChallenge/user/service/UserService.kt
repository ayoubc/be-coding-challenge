package de.dkb.api.codeChallenge.user.service

import de.dkb.api.codeChallenge.notification.model.NotificationDto
import de.dkb.api.codeChallenge.user.model.User
import de.dkb.api.codeChallenge.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(user: User) = userRepository.save(user)
    fun getAllUsers() = userRepository.findAll()

    fun sendNotification(notificationDto: NotificationDto) =
        userRepository.findById(notificationDto.userId)
            .filter { it.notifications.contains(notificationDto.notificationType) }
            .ifPresent { // Logic to send notification to user
                println(
                    "Sending notification of type ${notificationDto.notificationType}" +
                            " to user ``````${it.id}: ${notificationDto.message}"
                )
            }
}