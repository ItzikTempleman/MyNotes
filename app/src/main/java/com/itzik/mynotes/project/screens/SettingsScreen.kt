package com.itzik.mynotes.project.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsScreen(
    noteViewModel: NoteViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (icon) = createRefs()

        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = colorResource(id = R.color.blue_green),
            modifier = Modifier
                .padding(start = 4.dp, top = 16.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(26.dp)
        )
    }
}