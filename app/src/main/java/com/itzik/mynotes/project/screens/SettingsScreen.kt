package com.itzik.mynotes.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.screens.sections.SettingsRows
import com.itzik.mynotes.project.screens.sections.SettingsRows.DeletedNotes.SettingItem
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsScreen(
    locationViewModel: LocationViewModel,
    noteViewModel: NoteViewModel,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,

    ) {


    val settingsItems = listOf(
        SettingsRows.DeletedNotes,
        SettingsRows.MyLocation,
        SettingsRows.SystemColor
    )

    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (icon, settingItems) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.very_light_deep_purple2),
                            Color.White
                        )
                    )
                )
        ) {

            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(70.dp)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = colorResource(id = R.color.deep_purple),
                        modifier = Modifier.size(30.dp),
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Settings",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.deep_purple),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }

        LazyColumn(
            modifier = modifier
                .constrainAs(settingItems) {
                    top.linkTo(icon.bottom)
                }
                .fillMaxWidth()
        ) {
            items(settingsItems) {
                SettingItem(
                    settingsRow = it,
                    modifier = modifier,
                    coroutineScope = coroutineScope,
                    navController = navController,
                    noteViewModel = noteViewModel,
                    locationViewmodel = locationViewModel
                )
            }
        }
    }
}