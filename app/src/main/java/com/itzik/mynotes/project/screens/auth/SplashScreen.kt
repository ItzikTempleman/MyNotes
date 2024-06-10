package com.itzik.mynotes.project.screens.auth


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.navigation.Graph.AUTHENTICATION
import com.itzik.mynotes.project.screens.navigation.Graph.HOME
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SplashScreen(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    userViewModel: UserViewModel
) {
    var userList by remember {
        mutableStateOf(mutableListOf<User>())
    }

    LaunchedEffect(key1 = true) {
        delay(1500)
        coroutineScope.launch {
            userViewModel.fetchLoggedInUsers().collect {
                userList = it
            }

            if (userList.isNotEmpty() && userList.first().isLoggedIn) {
                navController.popBackStack()
                navController.navigate(HOME)

            } else {
                navController.popBackStack()
                navController.navigate(AUTHENTICATION)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    colorResource(id = R.color.blue_green),
                    Color.White
                )
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color.Gray
        )
    }
}