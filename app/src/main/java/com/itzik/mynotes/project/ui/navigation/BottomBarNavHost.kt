package com.itzik.mynotes.project.ui.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.gms.maps.model.LatLng
import com.itzik.mynotes.project.ui.navigation.Graph.HOME
import com.itzik.mynotes.project.ui.screen_sections.BottomBarScreen
import com.itzik.mynotes.project.ui.screens.HomeScreen
import com.itzik.mynotes.project.ui.screens.LikedNotesScreen
import com.itzik.mynotes.project.ui.screens.NoteScreen
import com.itzik.mynotes.project.ui.screens.ProfileScreen
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun BottomBarNavHost(
    userId:String,
    locationViewModel: LocationViewModel,
    context: Context,
    noteViewModel: NoteViewModel,
    newNavController: NavHostController = rememberNavController(),
    paramNavController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel,
    coroutineScope: CoroutineScope,
    currentLocation: LatLng,
    locationRequired: Boolean,
    startLocationUpdates: () -> Unit,
    updateIsLocationRequired: (Boolean) -> Unit,
) {
    var isNoteScreenVisible by remember {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            if (isNoteScreenVisible) {
                BottomBarScreen(
                    navController = newNavController
                )
            }
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = newNavController,
            startDestination = HOME
        ) {
            navigation(
                route = HOME,
                startDestination = Screen.Home.route
            ) {
                composable(route = Screen.Home.route) {
                    isNoteScreenVisible = true
                    HomeScreen(
                        userViewModel=userViewModel,
                        userId=userId,
                        locationViewModel = locationViewModel,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        navController = newNavController,
                        context = context,
                        updateIsLocationRequired = updateIsLocationRequired,
                        locationRequired = locationRequired,
                        startLocationUpdates = { startLocationUpdates() },
                        currentLocation = currentLocation,
                    )
                }

                composable(route = Screen.LikedNotes.route) {
                    isNoteScreenVisible = true
                    LikedNotesScreen(
                        userId=userId,
                        noteViewModel=noteViewModel,
                        coroutineScope = coroutineScope,
                        navController = newNavController,
                    )
                }

                composable(route = Screen.Profile.route) {
                    isNoteScreenVisible = true
                    ProfileScreen(
                        userId=userId,
                        modifier = Modifier,
                        navController = paramNavController,
                        userViewModel = userViewModel,
                        coroutineScope = coroutineScope,
                        noteViewModel = noteViewModel
                    )
                }


                composable(route = Screen.NoteScreen.route){
                    isNoteScreenVisible = false
                    val noteId = newNavController.previousBackStackEntry?.savedStateHandle?.get<Int>("noteId")
                    NoteScreen(
                        noteId = noteId,
                        paramNavController = paramNavController,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                    )
                }
            }
        }
    }
}
