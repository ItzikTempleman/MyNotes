package com.itzik.mynotes.project.ui.screen_sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.navigation.Screen

@Composable
fun BottomBarScreen(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screens = listOf(Screen.Home, Screen.LikedNotes, Screen.Profile)

    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth().height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach {
            BottomBarItem(
                index = screens.indexOf(it),
                screen = it,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun BottomBarItem(
    index: Int,
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    val selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    val contentColor = if (selected) when (index) {
        0 ->Color.DarkGray
        1 -> colorResource(id = R.color.muted_yellow)
        else -> Color.DarkGray
    } else Color.LightGray



    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White)
            .clickable {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            screen.icon?.let {
                Icon(
                    modifier = Modifier.size(34.dp),
                    imageVector = it,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }
    }
}


