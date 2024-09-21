package com.itzik.mynotes.project.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Constants {
    const val NAX_PINNED_NOTES = 3
    const val USER_TABLE = "userTable"
    const val NOTE_TABLE = "noteTable"

    const val BASE_URL = "https://pixabay.com/api/"
    const val API_KEY_VALUE = "46102404-ad517a06d05ad3776aea392e3"

}
fun reverseDateFormat(date: String): String {
    val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDate.format(formatter)
}

