package com.itzik.mynotes.project.ui.registrations

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.composable_elements.CustomButton
import com.itzik.mynotes.project.ui.composable_elements.CustomOutlinedTextField
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (title, cardContainer) = createRefs()

        Image(painter = painterResource(id = R.drawable.ripple), modifier = Modifier.fillMaxSize(),contentDescription = null, contentScale = ContentScale.FillHeight)

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 40.dp)
                .zIndex(2f),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.White,
            text = stringResource(id = R.string.welcome)
        )

        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp) ,
                colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .constrainAs(cardContainer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .padding(top = 120.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)

        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {

                val (emailTF, passwordTF, loginBtn, signUpBtn) = createRefs()



                CustomOutlinedTextField(
                    value = email,
                    thisValueChange = {
                        email = it
                        updateButtonState(email, password)
                    },
                    label = emailLabelMessage,
                    modifier = Modifier
                        .constrainAs(emailTF) {
                            top.linkTo(parent.top)
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
                        }
                        .padding(horizontal = 8.dp),
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
                        if (userViewModel.validateEmail(email) && userViewModel.validatePassword(
                                password
                            )
                        ) {
                            coroutineScope.launch {
                                userViewModel.getUserFromUserNameAndPassword(email, password)
                                    .collect { user ->
                                        if (user != null) {
                                            user.isLoggedIn = true
                                            userViewModel.updateIsLoggedIn(user)
                                            navController.popBackStack()
                                            navController.navigate(Screen.Home.route)
                                        } else {
                                            Log.e(
                                                "LoginScreen",
                                                "Invalid credentials or user not found"
                                            )
                                        }
                                    }
                            }
                        } else {
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
    }
}