package com.itzik.mynotes.project.ui.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
    data object ResetPassword : Screen(route = "resetPassword")

    data object Home : Screen(route = "home", title = "Home", icon = Icons.Default.Home)
    data object LikedNotes : Screen(route = "liked_notes", title = "liked notes", icon = Icons.Default.Star)
    data object Profile : Screen(route = "profile", title = "Profile", icon = Icons.Default.Person)


    data object NoteScreen : Screen(route = "note_screen", title = "Note Screen")
    data object DeletedNotesScreen : Screen(route = "deleted_notes", title = "Deleted notes", icon = Icons.Default.DeleteForever)
}

