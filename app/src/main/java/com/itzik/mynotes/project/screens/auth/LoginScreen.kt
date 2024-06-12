package com.itzik.mynotes.project.screens.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.screens.sections.CustomButton
import com.itzik.mynotes.project.screens.sections.CustomOutlinedTextField
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    var email by remember { mutableStateOf("") }
    val emailText = stringResource(id = R.string.email)
    var emailLabelMessage by remember { mutableStateOf(emailText) }
    var isEmailError by remember { mutableStateOf(false) }
    val passwordText = stringResource(id = R.string.password)
    var passwordLabelMessage by remember { mutableStateOf(passwordText) }
    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var isButtonEnabled by remember {
        mutableStateOf(false)
    }

    fun updateButtonState(email: String, password: String) {
        isButtonEnabled = email.isNotBlank() && password.isNotBlank()
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.blue_green),
                        Color.White
                    )
                )
                ),
    ) {
        val (title, emailTF, passwordTF, loginBtn, signUpBtn) = createRefs()

        Card(
            colors = CardDefaults.cardColors(colorResource(id = R.color.very_light_green)),
            elevation = CardDefaults.cardElevation(24.dp),
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .width(340.dp)
                .height(80.dp)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.very_light_green))
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Login,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp),
                )

                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    text = stringResource(id = R.string.log_in_screen),
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }

            CustomOutlinedTextField(
                value = email,
                thisValueChange = {
                    email = it
                    updateButtonState(email, password)
                },
                label = emailLabelMessage,
                modifier  = Modifier
                    .constrainAs(emailTF) {
                        top.linkTo(title.bottom)
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                imageVector = Icons.Default.Email,
                isError = isEmailError,
                visualTransformation = VisualTransformation.None,
                tint = Color.Black,
                contentColor = Color.Black
            )

        CustomOutlinedTextField(
            value = password,
            thisValueChange = {
                password = it
                updateButtonState(email, password)
            },
            label = passwordLabelMessage,
            modifier = Modifier
                .constrainAs(passwordTF) {
                    top.linkTo(emailTF.bottom)
                }
                .fillMaxWidth()
                .padding(8.dp),
            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            isError = isPasswordError,
            isKeyboardPasswordType = true,
            isIconClickableParam = true,
            isPasswordToggleClicked = isPasswordVisible,
            isPasswordIconShowing = {
                isPasswordVisible = !isPasswordVisible
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            tint = Color.Black,
            contentColor = Color.Black
        )

        CustomButton(
            text = stringResource(id = R.string.log_in),
            modifier = Modifier
                .constrainAs(loginBtn) {
                    bottom.linkTo(signUpBtn.top)
                }.padding(horizontal = 8.dp),
            onButtonClick = {
                if (!userViewModel.validateEmail(email)) {
                    isEmailError = true
                    emailLabelMessage = "Invalid username / email format"
                } else {
                    isEmailError = false
                    emailLabelMessage = emailText
                }
                if (!userViewModel.validatePassword(password)) {
                    isPasswordError = true
                    passwordLabelMessage = "Enter symbols of type format X, x, $ , 1"
                } else {
                    isPasswordError = false
                    passwordLabelMessage = passwordText
                }
                if (userViewModel.validateEmail(email) && userViewModel.validatePassword(password)) {
                    coroutineScope.launch {
                        userViewModel.getUserFromUserNameAndPassword(email, password)
                            .collect { user ->
                                if (user != null) {
                                    user.isLoggedIn = true
                                    userViewModel.updateIsLoggedIn(user)
                                    navController.popBackStack()
                                    navController.navigate(Screen.Home.route)
                                } else {
                                    // Handle incorrect credentials or user not found
                                    Log.e("LoginScreen", "Invalid credentials or user not found")
                                }
                            }
                    }
                } else {
                    // Handle validation errors
                    Log.e("LoginScreen", "Invalid email or password format")
                }
            },
            isEnabled = isButtonEnabled,
            fontSize = 20.sp,
            containerColor = Color.Gray,
            contentColor = Color.White,
            roundedShape = 8.dp,
            borderColor = Color.Gray
        )

        TextButton(
            onClick = {
                navController.navigate(Screen.Registration.route)
            },
            modifier = Modifier
                .constrainAs(signUpBtn) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.register),
                color = Color.Gray,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}