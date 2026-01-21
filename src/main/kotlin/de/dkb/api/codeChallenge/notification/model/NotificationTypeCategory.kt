package de.dkb.api.codeChallenge.notification.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull


@Entity
@Table(name = "notification_types")
data class NotificationTypeCategory(
    @Id
    val name: String,
    @field:NotNull
    var category: String
){
    // Default constructor for Hibernate
    constructor() : this("", "")
}
