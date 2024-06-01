package com.itzik.mynotes.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val dialogColor = Color(0xFF5F5F5F)

sealed class ThemeColors(
    val backGroundColor: Color,
    val surface: Color,
    val primary: Color,
    val text: Color
) {
    data object Night : ThemeColors(
        backGroundColor = Color(0xFF000000),
        surface = Color(0xFF000000),
        primary = Color(0xFF4FB64C),
        text = Color(0xFFFFFFFF)
    )

    data object Day : ThemeColors(
        backGroundColor = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        primary = Color(0xFFE5C966),
        text = Color(0xFF000000)
    )
}

