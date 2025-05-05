package com.arbuzerxxl.vibeshot.domain.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun String.formatUnixTimeWithSystemLocale(): String {

    val instant = Instant.ofEpochSecond(this.toLong())

    val formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG)
        .withLocale(Locale.getDefault())

    return instant.atZone(ZoneId.systemDefault())
        .format(formatter)

}

fun String.formatDateTimeWithLocale(): String {

    val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(this, formatterInput)

    val outputFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG)
        .withLocale(Locale.getDefault())

    return dateTime.format(outputFormatter)
}