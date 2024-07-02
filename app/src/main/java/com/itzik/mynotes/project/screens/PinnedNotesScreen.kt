package com.itzik.mynotes.project.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.sections.CircleWithIcon
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun PinnedNotesScreen(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (icon) = createRefs()
        CircleWithIcon(
            circleColor = Color.White,
            circleSize = 130.dp ,
            iconSize = 70.dp,
            tint = colorResource(id = R.color.navy_blue),
            imageVector = Icons.Default.PushPin,
            borderColor = Color.Black,
            borderThickness = 1.dp,
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.graphicsLayer(rotationZ = 20f)
                .padding(40.dp)

        )
    }
}

