package com.itzik.mynotes.project.ui.registrations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun ResetPasswordScreen(
    coroutineScope: CoroutineScope,
    rootNavController: NavHostController,
    userViewModel: UserViewModel?,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (screenTitle, enterPhoneNumberText)= createRefs()
        Text(
            modifier = Modifier.constrainAs(screenTitle){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }.padding(8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.reset_screen)
        )

        Text(
            modifier = Modifier.constrainAs(enterPhoneNumberText){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontSize = 22.sp,
            text = stringResource(R.string.enter_phone_number_to_reset_with)
        )

        
    }
}
