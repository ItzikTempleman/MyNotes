package com.itzik.mynotes.project.screens.sections

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class SettingsRows(
    var title: String,
    var icon: ImageVector,
    var tint: Color,
    var onClick: ((noteViewModel: NoteViewModel, coroutineScope: CoroutineScope, navController: NavHostController) -> Unit)? = null
) {
    data object DeletedNotes : SettingsRows(
        title = "Deleted notes",
        icon = Icons.Default.FolderDelete,
        onClick = { noteViewModel, coroutineScope, navController ->
            coroutineScope.launch {
                navController.navigate(Screen.DeletedNotesScreen.route)
            }
        },
        tint = Color.Gray
        )


    data object MyLocation :SettingsRows(
        title = "Current location: ",
        icon = Icons.Default.ShareLocation,
        tint = Color.Gray
    )


    data object SystemColor : SettingsRows(
        title = "Dark Mode", icon = Icons.Default.DarkMode,
        tint = Color.Gray
    )



    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun SettingItem(
        updatedLocationName: (String) -> Unit,
        noteViewModel: NoteViewModel,
        coroutineScope: CoroutineScope,
        navController: NavHostController,
        settingsRow: SettingsRows,
        modifier: Modifier
    ) {

        var isToggled by remember { mutableStateOf(false) }
        ConstraintLayout(
            modifier = modifier
                .clickable {
                    settingsRow.onClick?.let { it(noteViewModel, coroutineScope, navController) }
                }
                .fillMaxWidth()
                .height(50.dp),
        ) {
            val (darkModeToggle, icon, text, divider) = createRefs()

            if (settingsRow.title == "Dark Mode" || settingsRow.title == "Light Mode") {
                Switch(modifier = Modifier
                    .constrainAs(darkModeToggle) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(4.dp),
                    checked = isToggled,
                    onCheckedChange = {
                        isToggled = it
                        settingsRow.title = if (!isToggled) "Dark Mode" else "Light Mode"
                        settingsRow.icon =
                            if (!isToggled) Icons.Default.DarkMode else Icons.Default.LightMode
                    }
                )
            }


            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }, imageVector = settingsRow.icon, contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(icon.end)
                    }, text = if(settingsRow.title=="Current location: ")settingsRow.title +"$updatedLocationName" else settingsRow.title
            )
            HorizontalDivider(modifier = Modifier.constrainAs(divider) {
                bottom.linkTo(parent.bottom)
            }
            )
        }
    }
}
