package com.itzik.mynotes.project.ui.navigation

import android.annotation.SuppressLint
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
import androidx.navigation.navigation
import com.itzik.mynotes.project.ui.navigation.Graph.HOME
import com.itzik.mynotes.project.ui.screen_sections.BottomBarScreen
import com.itzik.mynotes.project.ui.screens.DeletedNotesScreen
import com.itzik.mynotes.project.ui.screens.HomeScreen
import com.itzik.mynotes.project.ui.screens.LikedNotesScreen
import com.itzik.mynotes.project.ui.screens.NoteScreen
import com.itzik.mynotes.project.ui.screens.ProfileScreen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun BottomBarNavHost(
    bottomBarNavController:NavHostController,
    userId: String,
    noteViewModel: NoteViewModel,
    rootNavController: NavHostController,
    userViewModel: UserViewModel,
    coroutineScope: CoroutineScope,
) {
    var isBottomBarVisible by remember {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                BottomBarScreen(
                    navController = bottomBarNavController
                )
            }
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = bottomBarNavController,
            startDestination = HOME
        ) {
            navigation(
                route = HOME,
                startDestination = Screen.Home.route
            ) {
                composable(route = Screen.Home.route) {
                    isBottomBarVisible = true
                    HomeScreen(
                        userViewModel=userViewModel,
                        userId=userId,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        bottomBarNavController = bottomBarNavController,
                    )
                }

                composable(route = Screen.LikedNotes.route) {
                    isBottomBarVisible = true
                    LikedNotesScreen(
                        noteViewModel=noteViewModel,
                        coroutineScope = coroutineScope,
                        bottomBarNavController = bottomBarNavController,
                    )
                }

                composable(route = Screen.Profile.route) {
                    isBottomBarVisible = true
                    ProfileScreen(
                        rootNavController = rootNavController,
                        userViewModel = userViewModel,
                        bottomBarNavController = bottomBarNavController,
                        coroutineScope = coroutineScope,
                        noteViewModel = noteViewModel
                    )
                }

                composable(route = Screen.NoteScreen.route){
                    isBottomBarVisible = false
                    val noteId = bottomBarNavController.previousBackStackEntry?.savedStateHandle?.get<Int>("noteId")
                        NoteScreen(
                            noteId = noteId,
                            bottomBarNavController = bottomBarNavController,
                            noteViewModel = noteViewModel,
                            coroutineScope = coroutineScope,
                        )
                    }

                composable(route = Screen.DeletedNotesScreen.route) {
                    isBottomBarVisible = true
                    DeletedNotesScreen(
                        modifier = Modifier,
                        userId=userId,
                        userViewModel = userViewModel,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        bottomBarNavController=bottomBarNavController
                    )
                }
            }
        }
    }
}
