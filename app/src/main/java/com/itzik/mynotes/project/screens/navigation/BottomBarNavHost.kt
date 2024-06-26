package com.itzik.mynotes.project.screens.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.gms.maps.model.LatLng
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.HomeScreen
import com.itzik.mynotes.project.screens.PinnedNotesScreen
import com.itzik.mynotes.project.screens.ProfileScreen
import com.itzik.mynotes.project.screens.SettingsScreen
import com.itzik.mynotes.project.screens.navigation.Graph.HOME
import com.itzik.mynotes.project.screens.note_screens.DeletedNotesScreen
import com.itzik.mynotes.project.screens.note_screens.NoteScreen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun BottomBarNavHost(
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
    user: User,
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
                        noteViewModel = noteViewModel,
                        modifier = Modifier.background(Color.White),
                        userViewModel = userViewModel,
                        coroutineScope = coroutineScope,
                        navController = newNavController,
                        user = user,
                        context = context,
                        updateIsLocationRequired = updateIsLocationRequired,
                        locationRequired = locationRequired,
                        startLocationUpdates = { startLocationUpdates() },
                        currentLocation = currentLocation,
                    )
                }

                composable(route = Screen.PinnedNotes.route) {
                    isNoteScreenVisible = true
                    PinnedNotesScreen(
                        modifier = Modifier.background(Color.White),
                        userViewModel = userViewModel,
                        coroutineScope = coroutineScope,
                        navController = newNavController,
                        user = user
                    )
                }

                composable(route = Screen.Settings.route) {
                    isNoteScreenVisible = true
                    SettingsScreen(
                        userViewModel = userViewModel,
                        noteViewModel = noteViewModel,
                        modifier = Modifier.background(Color.White),
                        navController = newNavController,
                        coroutineScope = coroutineScope,
                    )
                }

                composable(route = Screen.Profile.route) {
                    isNoteScreenVisible = true
                    ProfileScreen(
                        modifier = Modifier.background(Color.White),
                        navController = paramNavController,
                        userViewModel = userViewModel,
                        coroutineScope = coroutineScope,
                        user = user
                    )
                }

                composable(route = Screen.NoteScreen.route) {
                    isNoteScreenVisible = false
                    NoteScreen(
                        note = it.savedStateHandle.get<Note>(
                            "note"
                        ) ?: Note(content = ""),
                        modifier = Modifier.background(Color.White),
                        paramNavController = paramNavController,
                        userViewModel = userViewModel,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        user = user
                    )
                }
                composable(route = Screen.DeletedNotesScreen.route) {
                    isNoteScreenVisible = false
                    DeletedNotesScreen(
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        navController = paramNavController,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}










