package com.zetta.tiksid.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDateToYear(input: String): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormatter.parse(input)

    val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
    return yearFormatter.format(date!!)
}

fun formatDateToDateTimeMinute(input: String): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = dateFormatter.parse(input)

    val dateTimeFormatter = SimpleDateFormat("yyyy MMM dd HH:mm", Locale.getDefault())
    return dateTimeFormatter.format(date!!)
}