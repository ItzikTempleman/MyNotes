package com.itzik.mynotes.project.ui.navigation


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.android.gms.maps.model.LatLng
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.ui.DeletedNotesScreen
import com.itzik.mynotes.project.ui.SettingsScreen
import com.itzik.mynotes.project.ui.auth.LoginScreen
import com.itzik.mynotes.project.ui.auth.RegistrationScreen
import com.itzik.mynotes.project.ui.auth.SplashScreen
import com.itzik.mynotes.project.ui.navigation.Graph.AUTHENTICATION
import com.itzik.mynotes.project.ui.navigation.Graph.HOME
import com.itzik.mynotes.project.ui.navigation.Graph.ROOT
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope


@SuppressLint("MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)

@Composable
fun RootNavHost(
    locationViewModel: LocationViewModel,
    noteViewModel: NoteViewModel,
    context: Context,
    navController: NavHostController,
    userViewModel: UserViewModel,
    coroutineScope: CoroutineScope,
    currentLocation: LatLng,
    startLocationUpdates: () -> Unit,
    locationRequired: Boolean,
    updateIsLocationRequired: (Boolean) -> Unit,
) {


    val userList by userViewModel.publicLoggedInUsersList.collectAsState()
    val user = userList.firstOrNull() ?: User(userName = "", email = "", password = "", phoneNumber = 0)


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
                    locationViewModel=locationViewModel,
                    noteViewModel = noteViewModel,
                    context = context,
                    locationRequired = locationRequired,
                    startLocationUpdates = { startLocationUpdates() },
                    currentLocation = currentLocation,
                    paramNavController = navController,
                    userViewModel = userViewModel,
                    coroutineScope = coroutineScope,
                    updateIsLocationRequired = updateIsLocationRequired,
                    user = user
                )
            }

            composable(route = Screen.Settings.route) {

                SettingsScreen(
                    locationViewModel = locationViewModel,
                    noteViewModel = noteViewModel,
                    navController = navController,
                    coroutineScope = coroutineScope,
                )
            }

            composable(route = Screen.DeletedNotesScreen.route) {
                DeletedNotesScreen(
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope,
                    navController = navController,
                )
            }
        }
    }
}






