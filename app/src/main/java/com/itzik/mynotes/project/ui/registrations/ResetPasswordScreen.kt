package com.itzik.mynotes.project.ui.registrations

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Textsms
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Gender
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.ui.composable_elements.CustomOutlinedTextField
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun ResetPasswordScreen(
    coroutineScope: CoroutineScope,
    rootNavController: NavHostController,
    userViewModel: UserViewModel?,
) {

    var fetchedTempUser by remember {
        mutableStateOf(
            User("", "", "", "", false, "", "", Gender.MALE, "")
        )
    }
    val activity = LocalContext.current as Activity
    val mAuth = FirebaseAuth.getInstance()


    var associatedEmail by remember {
        mutableStateOf("")
    }

    var receivedCode by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var wasSMSSent by remember {
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
            if (!wasSMSSent) {
                TextField(
                    value = associatedEmail,
                    onValueChange = {
                        associatedEmail = it
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedIndicatorColor = Color.Gray,
                        disabledIndicatorColor = Color.Gray
                    ),
                    leadingIcon = {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Rounded.Email
                        )
                    },

                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (associatedEmail.isNotBlank()) {
                                    coroutineScope.launch {
                                        userViewModel?.getTempUserForVerification(associatedEmail)?.collect {
                                                fetchedTempUser = it
                                            }
                                        Log.d("TAG", "phone number sent: ${fetchedTempUser.phoneNumber}")
                                        val options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(fetchedTempUser.phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(activity).setCallbacks(object :
                                                PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                                                    Log.i("TAG", "Verification Completed")
                                                    Toast.makeText(activity, "Verification Completed", Toast.LENGTH_SHORT).show()
                                                }
                                                override fun onVerificationFailed(p0: FirebaseException) {
                                                    Log.e("TAG", "Verification failed", p0)
                                                    Toast.makeText(activity, "Verification Failed", Toast.LENGTH_SHORT).show()
                                                }
                                                override fun onCodeSent(
                                                    verificationCode: String, p1: PhoneAuthProvider.ForceResendingToken) {
                                                    super.onCodeSent(verificationCode, p1)
                                                    Log.i("TAG", "Otp Send Successfully")
                                                    receivedCode = verificationCode
                                                    Toast.makeText(activity, "Otp Send Successfully", Toast.LENGTH_SHORT).show()
                                                }
                                              }
                                            ).build()
                                        PhoneAuthProvider.verifyPhoneNumber(options)
                                    }
                                    wasSMSSent = true
                                }
                            }
                        ) {
                            Icon(
                                contentDescription = null,
                                imageVector = Icons.Rounded.Send
                            )
                        }
                    }, label = {
                        Text(text = stringResource(R.string.your_email))
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
                                //TODO ENTER THE CODE SENT BY SMS
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

                
                CustomOutlinedTextField(
                    invokedFunction = {
                        wasCodeCorrect = true
                        //TODO UPDATE NEW PASSWORD
                    },
                    isTrailingIconExist = true,
                    value = newPassword,
                    label = stringResource(R.string.enter_new_password),
                    imageVector =  Icons.Rounded.ArrowForward,
                    thisValueChange = {
                        newPassword = it
                    }
                )
            }
        }
    }
}