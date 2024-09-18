package com.itzik.mynotes.project.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Constants {
    const val NAX_PINNED_NOTES = 3
    const val USER_TABLE = "userTable"
    const val NOTE_TABLE = "noteTable"

}
fun reverseDateFormat(date: String): String {
    val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDate.format(formatter)
}
