package com.itzik.mynotes.project.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
        val (icon, title) = createRefs()

        Icon(
            tint = colorResource(id = R.color.blue_green),
            imageVector = Icons.Default.AttachFile,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 4.dp, top = 16.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(26.dp)
        )

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(icon.end)
                    top.linkTo(parent.top)
                }
                .padding(start = 4.dp,  top = 18.dp),
            text = "Pinned notes",
            fontSize = 20.sp,
            color = colorResource(id = R.color.blue_green),
            fontWeight = FontWeight.Bold
        )
    }
}