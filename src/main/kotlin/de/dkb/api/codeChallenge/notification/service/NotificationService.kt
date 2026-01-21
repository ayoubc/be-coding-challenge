package de.dkb.api.codeChallenge.notification.service

import de.dkb.api.codeChallenge.notification.model.NotificationDto
import de.dkb.api.codeChallenge.notification.model.NotificationTypeCategory
import de.dkb.api.codeChallenge.notification.repository.NotificationTypesRepository
import de.dkb.api.codeChallenge.user.model.User
import de.dkb.api.codeChallenge.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val userRepository: UserRepository,
    private val notificationTypesRepository: NotificationTypesRepository
) {

    fun registerUser(user: User) = userRepository.save(user)

    fun sendNotification(notificationDto: NotificationDto) {
        val notificationTypes = notificationTypesRepository.findAll()

        val notificationCategory = notificationTypes.orEmpty()
            .filter { it.name == notificationDto.notificationType }
            .map { it.category }
            .toSet()

        userRepository.findById(notificationDto.userId).ifPresent { user ->
            run {
                val isSubscribed = notificationTypes.orEmpty()
                    .filter { user.notifications.contains(it.name) }
                    .map { it.category }
                    .any { notificationCategory.contains(it) }
                if (isSubscribed) {
                    println(
                        "Sending notification of type ${notificationDto.notificationType}" +
                            " to user ``````${user.id}: ${notificationDto.message}"
                    )
                }
            }
        }
    }

    fun addNotificationType(notificationType: NotificationTypeCategory) = notificationTypesRepository.save(notificationType)

}