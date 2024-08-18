package com.itzik.mynotes.project.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.ui.Graph.AUTHENTICATION
import com.itzik.mynotes.project.ui.Graph.HOME
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SplashScreen(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    userViewModel: UserViewModel
) {
    val loggedInUsers by userViewModel.publicLoggedInUsersList.collectAsState()


    LaunchedEffect(loggedInUsers) {
        delay(1500)
        if (loggedInUsers.isNotEmpty() && loggedInUsers.first().isLoggedIn) {
            navController.popBackStack()
            navController.navigate(HOME)
        } else {
            navController.popBackStack()
            navController.navigate(AUTHENTICATION)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color.Gray
        )
    }
}