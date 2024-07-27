package com.itzik.mynotes.project.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PowerSettingsNew
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
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class ProfileRows(
    var itemTitle: String,
    var itemIcon: ImageVector,
    var onClick: ((
        noteViewModel: NoteViewModel,
        coroutineScope: CoroutineScope,
        navController: NavHostController,
        userViewModel: UserViewModel,
        user: User
    )
    -> Unit)? = null
) {
    data object Settings : ProfileRows(
        itemTitle = "Settings",
        itemIcon = Icons.Default.Settings,
        onClick = { noteViewModel, coroutineScope, navController, userViewModel, user ->
            navController.navigate(Screen.Settings.route)
        }
    )

    data object DeletedItems : ProfileRows(
        itemTitle = "Deleted notes",
        itemIcon = Icons.Default.DeleteForever,
        onClick = { noteViewModel, coroutineScope, navController, userViewModel, user ->
            navController.navigate(Screen.DeletedNotesScreen.route)
        }
    )

    data object LogOut : ProfileRows(
        itemTitle = "Log out",
        itemIcon = Icons.Default.PowerSettingsNew,
        onClick = { noteViewModel, coroutineScope, navController, userViewModel, user ->
            coroutineScope.launch {
                user.isLoggedIn = false
                userViewModel.updateIsLoggedIn(user)
                navController.navigate(Screen.Login.route)
            }
        }
    )
}


@Composable
fun ProfileItem(
    modifier: Modifier,
    profileItem: ProfileRows,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User
) {

    ConstraintLayout(
        modifier = modifier
            .clickable {
                profileItem.onClick?.let {
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
            imageVector = profileItem.itemIcon,
            contentDescription = null,
            tint = if(profileItem.itemTitle=="Log out") Color.Red else Color.Black
        )

        Text(
            modifier = Modifier
                .padding(4.dp)
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(icon.end)
                },
            text =profileItem.itemTitle,
            fontSize = 16.sp,
            color = if(profileItem.itemTitle=="Log out") Color.Red else Color.Black
        )

        HorizontalDivider(
            modifier = Modifier
                .constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 8.dp)
        )
    }
}