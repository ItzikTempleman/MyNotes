package com.itzik.mynotes.project.screens.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


object Graph {
    const val ROOT = "ROOT"
    const val AUTHENTICATION = "AUTHENTICATION"
    const val HOME = "HOME"
}

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null,
) {
    data object Splash : Screen(route = "splash")
    data object Login : Screen(route = "login")
    data object Registration : Screen(route = "registration")
    data object Home : Screen(route = "home", title = "Home", icon = Icons.Default.Home)
    data object PinnedNotes : Screen(route = "pinned_notes", title = "Pinned notes", icon = Icons.Default.AttachFile)
    data object Profile : Screen(route = "profile", title = "Profile", icon = Icons.Default.Person)
    data object Settings : Screen(route = "settings", title = "Settings", icon = Icons.Default.Settings)
    data object NoteScreen : Screen(route = "note_screen", title = "mote_screen", icon = Icons.Default.Add)
}

