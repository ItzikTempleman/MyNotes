package com.itzik.mynotes.project.ui.navigation


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.itzik.mynotes.project.ui.navigation.Graph.AUTHENTICATION
import com.itzik.mynotes.project.ui.navigation.Graph.HOME
import com.itzik.mynotes.project.ui.navigation.Graph.ROOT
import com.itzik.mynotes.project.ui.registrations.LoginScreen
import com.itzik.mynotes.project.ui.registrations.RegistrationScreen
import com.itzik.mynotes.project.ui.registrations.SplashScreen
import com.itzik.mynotes.project.ui.screens.DeletedNotesScreen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope


@SuppressLint("MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)

@Composable
fun RootNavHost(
    userId: String,
    noteViewModel: NoteViewModel,
    navController: NavHostController,
    userViewModel: UserViewModel,
    coroutineScope: CoroutineScope,
) {

    NavHost(
        startDestination = ROOT,
        navController = navController
    ) {
        navigation(
            startDestination = Screen.Splash.route,
            route = ROOT
        ) {
            composable(route = Screen.Splash.route) {
                SplashScreen(
                    navController = navController,
                    userViewModel = userViewModel,
                    coroutineScope = coroutineScope
                )
            }
        }

        navigation(
            startDestination = Screen.Login.route,
            route = AUTHENTICATION
        ) {
            composable(route = Screen.Login.route) {
                LoginScreen(
                    navController = navController,
                    userViewModel = userViewModel,
                    coroutineScope = coroutineScope,

                    )
            }

            composable(route = Screen.Registration.route) {
                RegistrationScreen(
                    navController = navController,
                    userViewModel = userViewModel,
                    coroutineScope = coroutineScope,

                    )
            }
        }

        navigation(
            startDestination = Screen.Home.route,
            route = HOME
        ) {
            composable(route = Screen.Home.route) {
                BottomBarNavHost(
                    noteViewModel = noteViewModel,
                    paramNavController = navController,
                    userViewModel = userViewModel,
                    coroutineScope = coroutineScope,
                    userId = userId,
                )
            }

            composable(route = Screen.DeletedNotesScreen.route) {
                DeletedNotesScreen(
                    modifier = Modifier,
                    userId=userId,
                    userViewModel = userViewModel,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope,
                    navController = navController,
                )
            }
        }
    }
}






