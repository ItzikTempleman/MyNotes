package com.itzik.mynotes.project.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.User
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



        Text(
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(12.dp),
            text = stringResource(id = R.string.pinned_notes),
            fontSize = 24.sp,
            color = colorResource(id = R.color.darker_blue),
            fontWeight = FontWeight.Bold
        )
    }
}

