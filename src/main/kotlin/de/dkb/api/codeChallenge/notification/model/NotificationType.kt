package de.dkb.api.codeChallenge.notification.model

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Suppress("EnumEntryName")
enum class NotificationType {
    type1,
    type2,
    type3,
    type4,
    type5,
}

@Converter
class NotificationTypeSetConverter : AttributeConverter<MutableSet<String>, String> {

    override fun convertToDatabaseColumn(valueSet: MutableSet<String>?): String =
        valueSet.orEmpty()
            .joinToString(separator = ";") { it }

    override fun convertToEntityAttribute(databaseString: String?): MutableSet<String> =
        databaseString.orEmpty()
            .split(";")
            .map { it }
            .toMutableSet()
}