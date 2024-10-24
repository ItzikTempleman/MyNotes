package com.itzik.mynotes.project.ui.registrations

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.itzik.mynotes.project.main.NoteApp
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


    var associatedEmail by remember { mutableStateOf("") }
    val createEmailText = stringResource(id = R.string.your_email)
    var createEmailLabelMessage by remember { mutableStateOf(createEmailText) }
    var isNewEmailError by remember { mutableStateOf(false) }

    val createPhoneNumberText = stringResource(id = R.string.enter_code_sent_to_you)
    val createPhoneNumberLabelMessage by remember { mutableStateOf(createPhoneNumberText) }
    val phoneNumberError by remember { mutableStateOf(false) }
    var receivedCode by remember {
        mutableStateOf("")
    }

    var wasSMSSent by remember {
        mutableStateOf(false)
    }
    var wasCodeCorrect by remember {
        mutableStateOf(false)
    }

    val createdPasswordText = stringResource(id = R.string.enter_new_password)
    var createPassword by remember { mutableStateOf("") }
    var createPasswordLabelMessage by remember { mutableStateOf(createdPasswordText) }
    var isCreatePasswordError by remember { mutableStateOf(false) }
    var isCreatedPasswordVisible by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {

        val (title, enterEmail, enterCode, newPasswordTF) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(16.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.reset_screen)
        )



        if (!wasSMSSent) {
            CustomOutlinedTextField(
                isIconClickableParam = true,
                value = associatedEmail,
                thisValueChange = {
                    associatedEmail = it
                },
                label = createEmailLabelMessage,
                modifier = Modifier
                    .constrainAs(enterEmail) {
                        top.linkTo(title.bottom)
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                rightImageVector = Icons.Rounded.Send,
                leftImageVector = Icons.Default.Email,
                isError = isNewEmailError,
                visualTransformation = VisualTransformation.None,
                tint = Color.Black,
                isTrailingIconExist = true,
                invokedFunction = {
                    if (associatedEmail.isNotBlank()) {
                        coroutineScope.launch {
                            userViewModel?.getTempUserForVerification(associatedEmail)
                                ?.collect {
                                    fetchedTempUser = it
                                }
                            Log.d("TAG", "phone number sent: ${fetchedTempUser.phoneNumber}")
                            val options = PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(fetchedTempUser.phoneNumber)
                                .setTimeout(60L, TimeUnit.SECONDS).setActivity(activity)
                                .setCallbacks(object :
                                    PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
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
                        wasSMSSent = true
                    }
                }
            )

        } else if (!wasCodeCorrect) {
            CustomOutlinedTextField(
                isIconClickableParam = true,
                value = receivedCode,
                thisValueChange = {
                    receivedCode = it
                },
                rightImageVector = Icons.Rounded.NavigateNext,
                label = createPhoneNumberLabelMessage,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(enterCode) {
                        top.linkTo(title.bottom)
                    }
                    .padding(8.dp),
                leftImageVector = Icons.Default.Sms,
                isError = phoneNumberError,
                visualTransformation = VisualTransformation.None,
                isTrailingIconExist = true,
                invokedFunction = {
                    wasCodeCorrect = receivedCode == "1"  //TODO change the value of 1 to the vlaue that was actually received
                },
                tint = Color.Black,
            )
        } else {
            CustomOutlinedTextField(
                isIconClickableParam = true,
                value = createPassword,
                thisValueChange = {
                    createPassword = it

                },
                rightImageVector = Icons.Default.ArrowCircleRight,
                invokedFunction = {
                    wasCodeCorrect = true
                    //TODO UPDATE NEW PASSWORD
                },
                label = createPasswordLabelMessage,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(newPasswordTF) {
                        top.linkTo(title.bottom)
                    }
                    .padding(8.dp),
                tint = Color.Black,
                leftImageVector = if (isCreatedPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                isError = isCreatePasswordError,
                isKeyboardPasswordType = true,
                isTrailingIconExist = true,
                isPasswordToggleClicked = isCreatedPasswordVisible,
                isPasswordIconShowing = {
                    isCreatedPasswordVisible = !isCreatedPasswordVisible
                },
                visualTransformation = if (isCreatedPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            )
        }
    }
}


fun makeToast(str: String, e: FirebaseException? = null) {
    Log.e("TAG", str, e)
    Toast.makeText(NoteApp.instance, str, Toast.LENGTH_SHORT).show()
}