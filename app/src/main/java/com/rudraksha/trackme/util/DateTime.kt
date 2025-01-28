package com.rudraksha.trackme.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toDateAndTime(): Pair<String, String> {
    // ThreadLocal for date and time formatters to ensure thread safety
    val dateFormatter = ThreadLocal.withInitial { DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd") }
    val timeFormatter = ThreadLocal.withInitial { DateTimeFormatter.ofPattern("HH:mm:ss") }

    // Convert the Long timestamp to Instant
    val instant = Instant.ofEpochMilli(this)

    // Get the date and time for the default system time zone
    val dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()

    // Format the date and time
    val formattedDate = dateFormatter.get()?.format(dateTime) ?: ""
    val formattedTime = timeFormatter.get()?.format(dateTime) ?: ""

    // Return formatted date and time as a Pair
    return Pair(formattedDate, formattedTime)
}
