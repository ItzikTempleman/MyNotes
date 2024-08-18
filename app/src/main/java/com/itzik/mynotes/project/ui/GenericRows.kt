package com.itzik.mynotes.project.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class GenericRows(
    var itemTitle: String,
    var itemIcon: ImageVector,
    var onClick: ((
        noteViewModel: NoteViewModel,
        coroutineScope: CoroutineScope,
        navController: NavHostController,
        userViewModel: UserViewModel,
        user: User,
    )
    -> Unit)? = null
) {
    data object Settings : GenericRows(
        itemTitle = "Settings",
        itemIcon = Icons.Default.Settings,
        onClick = { _, _, navController, _, _ ->
            navController.navigate(Screen.Settings.route)
        }
    )

    data object DeletedItems : GenericRows(
        itemTitle = "Deleted notes",
        itemIcon = Icons.Default.DeleteForever,
        onClick = { _, _, navController, _, _ ->
            navController.navigate(Screen.DeletedNotesScreen.route)
        }
    )

    data object LogOut : GenericRows(
        itemTitle = "Log out",
        itemIcon = Icons.Default.PowerSettingsNew,
        onClick = { _, coroutineScope, navController, userViewModel, user ->
            coroutineScope.launch {
                user.isLoggedIn = false
                userViewModel.updateIsLoggedIn(user)
                navController.navigate(Screen.Login.route)
            }
        }
    )

    data object RetrieveNote : GenericRows(
        itemTitle = "Retrieve note",
        itemIcon = Icons.Default.RestoreFromTrash,
        onClick = { noteViewModel, coroutineScope, _, _, _ ->
            coroutineScope.launch {

            }
        }
    )

    data object DeleteNote : GenericRows(
        itemTitle = "Delete note forever",
        itemIcon = Icons.Default.DeleteForever,
        onClick = { noteViewModel, coroutineScope, _, _, _ ->
            coroutineScope.launch {

            }
        }
    )
}

@Composable
fun GenericItem(
    modifier: Modifier,
    item: GenericRows,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User,
) {

    ConstraintLayout(
        modifier = modifier
            .clickable {
                item.onClick?.let {
                    it(
                        noteViewModel,
                        coroutineScope,
                        navController,
                        userViewModel,
                        user
                    )

                }
            }
            .fillMaxWidth()
            .height(50.dp),
    ) {
        val (icon, text, divider) = createRefs()

        Icon(
            modifier = Modifier
                .padding(4.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            imageVector = item.itemIcon,
            contentDescription = null,
            tint = if (item.itemTitle == "Log out") Color.Red else if(item.itemTitle == "Retrieve note") Color.Blue else Color.Black
        )

        Text(
            modifier = Modifier
                .padding(4.dp)
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(icon.end)
                },
            text = item.itemTitle,
            fontSize = 16.sp,
            color = if (item.itemTitle == "Log out") Color.Red else Color.Black
        )

        if (item.itemTitle != "Log out") {
            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(divider) {
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}