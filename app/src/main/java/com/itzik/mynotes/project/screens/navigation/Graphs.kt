package com.itzik.mynotes.project.screens.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector


object Graph {
    const val ROOT = "ROOT"
    const val AUTHENTICATION = "AUTHENTICATION"
    const val HOME = "HOME"
}

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    data object Splash : Screen(route = "splash")
    data object Login : Screen(route = "login")
    data object Registration : Screen(route = "registration")

    data object Home : Screen(route = "home", title = "Home", icon = Icons.Outlined.Home)
    data object LikedNotes : Screen(route = "liked_notes", title = "liked notes", icon = Icons.Outlined.StarBorder)
    data object Profile : Screen(route = "profile", title = "Profile", icon = Icons.Outlined.Person)
    data object NoteScreen : Screen(route = "note_screen", title = "Note Screen")

    data object DeletedNotesScreen : Screen(route = "deleted_notes", title = "Deleted notes", icon = Icons.Outlined.DeleteForever)
    data object Settings : Screen(route = "settings", title = "Settings", icon = Icons.Outlined.Settings)
}

