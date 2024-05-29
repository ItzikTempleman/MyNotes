package com.itzik.mynotes.project.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.screens.SettingsRows.DeletedNotes.isDarkModeSelected
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class SettingsRows(
    val title: String,
    val icon: ImageVector,
    var isDarkModeSelected: Boolean = false,
    val onClick: (
        noteViewModel: NoteViewModel,
        coroutineScope: CoroutineScope,
        navController: NavHostController
    ) -> Unit
) {
    data object DeletedNotes :
        SettingsRows(
            title = "Deleted notes",
            icon = Icons.Default.DeleteForever,
            onClick = { noteViewModel, coroutineScope, navController ->
                coroutineScope.launch {
                    navController.navigate(Screen.DeletedNotesScreen.route)
                }
            }
        )

    data object SystemColor : SettingsRows(
        title = "Theme color",
        icon = if (isDarkModeSelected) Icons.Default.DarkMode else Icons.Default.LightMode,
        onClick = { noteViewModel, coroutineScope, navController ->
            isDarkModeSelected = !isDarkModeSelected
            coroutineScope.launch {
                noteViewModel.setSystemColor(isDarkModeSelected)
            }
        }
    )

    @Composable
    fun SettingItem(
        noteViewModel: NoteViewModel,
        coroutineScope: CoroutineScope,
        navController: NavHostController,
        settingsRow: SettingsRows,
        modifier: Modifier
    ) {

        Row(
            modifier = modifier
                .border(
                    border = BorderStroke(0.5.dp, Color.Black)
                )
                .clickable {
                    settingsRow.onClick(noteViewModel, coroutineScope, navController)
                }
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = settingsRow.icon,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = settingsRow.title
            )
        }
    }
}