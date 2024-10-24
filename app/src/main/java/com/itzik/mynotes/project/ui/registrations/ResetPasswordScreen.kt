package com.itzik.mynotes.project.ui.registrations

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.itzik.mynotes.R
import com.itzik.mynotes.project.main.NoteApp
import com.itzik.mynotes.project.model.Gender
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.ui.composable_elements.EmailInputField
import com.itzik.mynotes.project.ui.composable_elements.NewPasswordInputField
import com.itzik.mynotes.project.ui.composable_elements.ResetPasswordState
import com.itzik.mynotes.project.ui.composable_elements.SmsInputField
import com.itzik.mynotes.project.ui.navigation.Screen
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
    var resetState by remember { mutableStateOf<ResetPasswordState>(ResetPasswordState.EnterEmail) }
    var fetchedTempUser by remember {
        mutableStateOf(
            User(
                "",
                "",
                "",
                "",
                false,
                "",
                "",
                Gender.MALE,
                ""
            )
        )
    }
    val activity = LocalContext.current as Activity
    val mAuth = FirebaseAuth.getInstance()
    var associatedEmail by remember { mutableStateOf("") }
    var receivedCode by remember { mutableStateOf("") }
    var createPassword by remember { mutableStateOf("") }
    var isCreatedPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.reset_screen)
        )

        when (resetState) {
            is ResetPasswordState.EnterEmail -> {
                EmailInputField(
                    email = associatedEmail,
                    onSendClick = {
                        if (associatedEmail.isNotBlank()) {
                            coroutineScope.launch {
                                userViewModel?.getTempUserForVerification(associatedEmail)
                                    ?.collect {
                                        fetchedTempUser = it
                                        Log.d(
                                            "TAG",
                                            "phone number sent: ${fetchedTempUser.phoneNumber}"
                                        )
                                        val options = PhoneAuthOptions.newBuilder(mAuth)
                                            .setPhoneNumber(fetchedTempUser.phoneNumber)
                                            .setTimeout(60L, TimeUnit.SECONDS).setActivity(activity)
                                            .setCallbacks(object :
                                                PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                override fun onVerificationCompleted(
                                                    phoneAuthCredential: PhoneAuthCredential,
                                                ) {
                                                    makeToast("Verification completed")
                                                }

                                                override fun onVerificationFailed(p0: FirebaseException) {
                                                    makeToast("Verification failed")
                                                }

                                                override fun onCodeSent(
                                                    verificationCode: String,
                                                    p1: PhoneAuthProvider.ForceResendingToken,
                                                ) {
                                                    super.onCodeSent(verificationCode, p1)
                                                    receivedCode = verificationCode
                                                    makeToast("Otp Send Successfully")
                                                }
                                            }
                                            ).build()
                                        PhoneAuthProvider.verifyPhoneNumber(options)
                                    }
                            }
                        }
                        resetState = ResetPasswordState.EnterCode
                    },
                    onEmailChanged = {
                        associatedEmail = it
                    })

            }

            is ResetPasswordState.EnterCode -> {
                SmsInputField(
                    code = receivedCode,
                    onCodeFilled = {
                        receivedCode = it
                        //wasCodeCorrect = receivedCode == "1"  //TODO change the value of 1 to the value that was actually received
                    },
                    onSubmitCodeClick = {
                        resetState = ResetPasswordState.EnterNewPassword
                    }
                )
            }

            ResetPasswordState.EnterNewPassword -> {
                NewPasswordInputField(
                    newPassword = createPassword,
                    onPasswordUpdated = {
                        createPassword = it
                    },
                    isPasswordVisible = isCreatedPasswordVisible,
                    onToggleVisibility = {
                        isCreatedPasswordVisible = !isCreatedPasswordVisible
                    },
                    onConfirmPasswordClick = {
                        coroutineScope.launch {
                            userViewModel?.updatePassword(createPassword)
                            fetchedTempUser.isLoggedIn = true
                            userViewModel?.updateIsLoggedIn(fetchedTempUser)
                            rootNavController.popBackStack()
                            rootNavController.navigate(Screen.Home.route)
                        }
                    }

                )

            }
        }

    }
}

fun makeToast(str: String, e: FirebaseException? = null) {
    Log.e("TAG", str, e)
    Toast.makeText(NoteApp.instance, str, Toast.LENGTH_SHORT).show()
}