package ru.rusatom.dev.digital.water.media.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateUtil {

    fun millisToTime(millis: Long): String {
        var seconds = 0
        var minutes = 0
        var hours = 0
        if (millis > 1000) {
            seconds = (millis / 1000).toInt()
            if (seconds > 60) {
                minutes = (seconds / 60)
                seconds %= 60
                if (minutes > 60) {
                    hours /= 60
                    minutes %= 60
                }
            }
        }
        return if (hours < 0 || minutes < 0 || seconds < 0) ""
        else if (hours > 0) "${if (hours > 9) "$hours" else "0$hours"}:${if (minutes > 9) "$minutes" else "0$minutes"}:${if (seconds > 9) "$seconds" else "0$seconds"}"
        else if (hours == 0 && minutes > 0) "${if (minutes > 9) "$minutes" else "0$minutes"}:${if (seconds > 9) "$seconds" else "0$seconds"}"
        else if (hours == 0 && minutes == 0) "0:${if (seconds > 9) "$seconds" else "0$seconds"}"
        else ""
    }

    fun dateToUtc(dateString: String?): String? {
        if (dateString == null) return null
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        if (dateString.length < 16) {
            return dateString
        }

        val ldt = LocalDateTime.parse(dateString, formatter)
        val ldtZoned: ZonedDateTime = ldt.atZone(ZoneId.systemDefault())
        val utcZoned: ZonedDateTime = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"))

        return utcZoned.format(formatter)
    }

    fun dateFromUtc(dateString: String?): String? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val userFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm")

        if (dateString == null || dateString.length < 16) {
            return dateString
        }

        val ldt = LocalDateTime.parse(dateString, formatter)
        val utcZoned: ZonedDateTime = ldt.atZone(ZoneId.of("UTC"))
        val ldtZoned: ZonedDateTime = utcZoned.withZoneSameInstant(ZoneId.systemDefault())

        return ldtZoned.format(userFormatter)
    }

    fun formatDate(dateString: String?): String? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val userFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm")

        if (dateString == null || dateString.length < 16) {
            return dateString
        }

        return try {
            userFormatter.format(formatter.parse(dateString))
        } catch (e: Exception) {
            dateString
        }
    }

}
