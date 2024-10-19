package com.itzik.mynotes.project.ui.registrations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Textsms
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

    var associatedEmail by remember {
        mutableStateOf("")
    }

    var receivedCode by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var wasEmailSent by remember {
        mutableStateOf(false)
    }
    var wasCodeCorrect by remember {
        mutableStateOf(false)
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {

        val (title, content) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.reset_screen)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(content) {
                    top.linkTo(title.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!wasEmailSent) {
                TextField(
                    leadingIcon = {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Rounded.Email
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                wasEmailSent = true
                            }
                        ) {
                            Icon(
                                contentDescription = null,
                                imageVector = Icons.Rounded.Send
                            )
                        }
                    }, label = {
                        Text(text = stringResource(R.string.your_email))
                    },
                    value = associatedEmail,
                    onValueChange = {
                        associatedEmail = it
                    }
                )
            } else {
                TextField(
                    leadingIcon = {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Rounded.Textsms
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                wasCodeCorrect = true
                            }
                        ) {
                            Icon(
                                contentDescription = null,
                                imageVector = Icons.Rounded.ArrowDownward
                            )
                        }
                    },
                    label = {
                        Text(text = stringResource(R.string.enter_code_sent_to_you))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = receivedCode,
                    onValueChange = {
                        receivedCode = it
                    }
                )

                TextField(
                    leadingIcon = {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Rounded.RemoveRedEye
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                wasCodeCorrect = true
                                //TODO LOGIN WITH NEW PASSWORD
                            }
                        ) {
                            Icon(
                                contentDescription = null,
                                imageVector = Icons.Rounded.ArrowForward
                            )
                        }
                    },
                    label = {
                        Text(text = stringResource(R.string.enter_new_password))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                    }
                )
            }
        }
    }
}