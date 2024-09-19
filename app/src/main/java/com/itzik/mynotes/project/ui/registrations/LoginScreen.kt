package com.itzik.mynotes.project.ui.registrations

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.composable_elements.CustomOutlinedTextField
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginScreen(
    coroutineScope: CoroutineScope,
    rootNavController: NavHostController,
    userViewModel: UserViewModel?=null
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (backgroundBox, loginTextTop, loginTextLine, loginTextBottom, cardContainer, doNotHaveText, signUpBtn) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(backgroundBox) {
                        top.linkTo(parent.top)
                        height = Dimension.percent(0.7f)
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(R.color.lighter_blue),
                                colorResource(R.color.darker_blue),
                            )
                        )
                    )
            ) {}
            Text(
                modifier = Modifier
                    .constrainAs(loginTextTop) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(loginTextLine.top)
                    }
                    .padding(top = 150.dp),
                fontSize = 36.sp,
                fontFamily = FontFamily.SansSerif,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                text = stringResource(id = R.string.notes)
            )

            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(loginTextLine) {
                        top.linkTo(loginTextTop.bottom, margin = (-20).dp)
                    }
                    .padding(horizontal = 125.dp),
                thickness = 1.dp,
                color = Color.White
            )

            Text(
                modifier = Modifier
                    .constrainAs(loginTextBottom) {
                        top.linkTo(loginTextLine.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                fontSize = 20.sp,
                color = Color.White,
                text = stringResource(id = R.string.manage)
            )


            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier
                    .constrainAs(cardContainer) {
                        top.linkTo(loginTextBottom.bottom)
                    }
                    .height(600.dp)
                    .fillMaxWidth()
                    .padding(30.dp)

            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val (title, emailTF, passwordTF, forgotPassword, loginBtn, orText, googleBtn, verticalLine, facebookBtn) = createRefs()


                    Text(
                        modifier = Modifier
                            .constrainAs(title) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(30.dp),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        text = stringResource(id = R.string.login_account)
                    )

                    CustomOutlinedTextField(
                        value = email,
                        thisValueChange = {
                            email = it
                            updateButtonState(email, password)
                        },
                        label = emailLabelMessage,
                        modifier = Modifier
                            .constrainAs(emailTF) {
                                top.linkTo(title.bottom)
                            }
                            .fillMaxWidth()
                            .padding(8.dp),
                        imageVector = Icons.Default.Email,
                        isError = isEmailError,
                        visualTransformation = VisualTransformation.None,
                        tint = Color.Black,
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
                    )

                    TextButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .constrainAs(forgotPassword) {
                                top.linkTo(passwordTF.bottom)
                                start.linkTo(parent.start)
                            }.padding(start = 20.dp),

                        ) {
                        Text(
                            text = stringResource(R.string.forgot)
                        )
                    }

                    Button(
                        enabled = isButtonEnabled,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.darker_blue)
                        ),
                        modifier = Modifier
                            .constrainAs(loginBtn) {
                                top.linkTo(forgotPassword.bottom)
                            }
                            .fillMaxWidth()
                            .padding(24.dp),
                        onClick = {
                            if (userViewModel != null) {
                                if (!userViewModel.validateEmail(email)) {
                                    isEmailError = true
                                    emailLabelMessage = "Invalid username / email format"
                                } else {
                                    isEmailError = false
                                    emailLabelMessage = emailText
                                }
                            }
                            if (userViewModel != null) {
                                if (!userViewModel.validatePassword(password)) {
                                    isPasswordError = true
                                    passwordLabelMessage =
                                        "Enter symbols of type format X, x, $ , 1"
                                } else {
                                    isPasswordError = false
                                    passwordLabelMessage = passwordText
                                }
                            }
                            if (userViewModel != null) {
                                if (userViewModel.validateEmail(email) && userViewModel.validatePassword(
                                        password
                                    )
                                ) {
                                    coroutineScope.launch {
                                        userViewModel.getUserFromUserNameAndPassword(
                                            email,
                                            password
                                        )
                                            .collect { user ->
                                                if (user != null) {
                                                    user.isLoggedIn = true
                                                    userViewModel.updateIsLoggedIn(user)
                                                    rootNavController.popBackStack()
                                                    rootNavController.navigate(Screen.Home.route)
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
                            }
                        }
                    ) {
                        Text(
                            fontSize = 20.sp,
                            text = stringResource(id = R.string.log_in)
                        )
                    }

                    Text(
                        modifier = Modifier.constrainAs(orText) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(loginBtn.bottom)
                        },
                        text = "OR",
                        fontSize = 14.sp
                    )

                    IconButton(
                        modifier = Modifier
                            .constrainAs(facebookBtn) {
                                top.linkTo(orText.bottom)
                                end.linkTo(verticalLine.start)
                            }
                            .size(60.dp)
                            .padding(12.dp),
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.facebook_logo),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.constrainAs(verticalLine) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        color = Color.Transparent
                    )

                    IconButton(
                        modifier = Modifier
                            .constrainAs(googleBtn) {
                                top.linkTo(orText.bottom)
                                start.linkTo(verticalLine.end)
                            }
                            .size(60.dp)
                            .padding(12.dp),
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.google_logo),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }
            }


            Text(
                modifier = Modifier
                    .constrainAs(doNotHaveText) {
                        top.linkTo(cardContainer.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    },
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp,
                text = stringResource(id = R.string.dont_have),
            )


            TextButton(
                onClick = {
                    rootNavController.navigate(Screen.Registration.route)
                },
                modifier = Modifier
                    .constrainAs(signUpBtn) {
                        top.linkTo(doNotHaveText.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
            ) {
                Text(
                    fontSize = 22.sp,
                    text = stringResource(id = R.string.register),
                )
            }
        }
    }
}


@Preview(showBackground = true, device = "spec:width=412dp,height=932dp")
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        coroutineScope = rememberCoroutineScope(),
        rootNavController = rememberNavController()
    )
}
