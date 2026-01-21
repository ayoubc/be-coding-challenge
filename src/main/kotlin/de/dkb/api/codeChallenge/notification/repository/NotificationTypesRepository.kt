package de.dkb.api.codeChallenge.notification.repository

import de.dkb.api.codeChallenge.notification.model.NotificationTypeCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationTypesRepository : JpaRepository<NotificationTypeCategory, String>