package com.itzik.mynotes.project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.composable_elements.SettingItem
import com.itzik.mynotes.project.ui.composable_elements.SettingsRows
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsScreen(
    locationViewModel: LocationViewModel,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,

    ) {


    val settingsItems = listOf(
        SettingsRows.MyLocation,
        SettingsRows.SystemColor
    )

    ConstraintLayout(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        val (returnIcon,title, settingItems) = createRefs()

        IconButton(
            modifier = Modifier
                .constrainAs(returnIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(8.dp)
                .size(32.dp),
            onClick = {
                navController.navigate(Screen.Home.route)
            }

        ) {
            Icon(
                tint = colorResource(id = R.color.blue_green),
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null
            )
        }

        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = null,
            tint = colorResource(id = R.color.blue_green),
            modifier = Modifier.padding( 8.dp).size(32.dp).constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(returnIcon.end)
            }
        )


        LazyColumn(
            modifier = Modifier
                .constrainAs(settingItems) {
                    top.linkTo(title.bottom)
                }
                .fillMaxWidth()
        ) {
            items(settingsItems) {
                SettingItem(
                    settingsRow = it,
                    coroutineScope = coroutineScope,
                    navController = navController,
                    noteViewModel = noteViewModel,
                    locationViewmodel = locationViewModel
                )
            }
        }
    }
}