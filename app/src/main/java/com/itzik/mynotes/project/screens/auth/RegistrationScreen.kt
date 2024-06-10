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
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
fun RegistrationScreen(
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val nameText = stringResource(id = R.string.full_name)
    val nameLabelMessage by remember { mutableStateOf(nameText) }
    var name by remember { mutableStateOf("") }
    val nameError by remember { mutableStateOf(false) }

    var createEmail by remember { mutableStateOf("") }
    val createEmailText = stringResource(id = R.string.create_email)
    var createEmailLabelMessage by remember { mutableStateOf(createEmailText) }

    var isNewEmailError by remember { mutableStateOf(false) }
    val createdPasswordText = stringResource(id = R.string.create_password)
    var createPassword by remember { mutableStateOf("") }
    var createPasswordLabelMessage by remember { mutableStateOf(createdPasswordText) }
    var isCreatePasswordError by remember { mutableStateOf(false) }
    var isCreatedPasswordVisible by remember { mutableStateOf(false) }

    var createPhoneNumber by remember { mutableStateOf("") }
    val createPhoneNumberText = stringResource(id = R.string.enter_phone_number)
    val createPhoneNumberLabelMessage by remember { mutableStateOf(createPhoneNumberText) }
    val phoneNumberError by remember { mutableStateOf(false) }

    var isButtonEnabled by remember { mutableStateOf(false) }

    fun updateButtonState(name: String, email: String, password: String, phoneNumber: String) {
        isButtonEnabled =
            userViewModel.validateName(name) &&
                    userViewModel.validateEmail(email) &&
                    userViewModel.validatePassword(password) &&
                    userViewModel.validatePhoneNumber(phoneNumber)
        Log.d("TAG","name: ${userViewModel.validateName(name)},email: ${userViewModel.validateEmail(email)},password: ${userViewModel.validatePassword(password)},phone: ${userViewModel.validatePhoneNumber(phoneNumber)} ")
    }

    ConstraintLayout(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.blue_green),
                        Color.White
                    )
                )
            )
            .fillMaxSize()
           ,
    ) {
        val (title, nameTF, emailTF, passwordTF, phoneNumberTF, signUpBtn) = createRefs()
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
                    imageVector = Icons.Default.AppRegistration,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp),
                )

                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    text = stringResource(id = R.string.registration),
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }


        CustomOutlinedTextField(
            value = name,
            thisValueChange = {
                name = it
                updateButtonState(name, createEmail, createPassword, createPhoneNumber)
            },
            label = nameLabelMessage,
            modifier = Modifier
                .constrainAs(nameTF) {
                    top.linkTo(title.bottom)
                }
                .fillMaxWidth()
                .padding(8.dp),
            imageVector = Icons.Default.Person,
            isError = nameError,
            visualTransformation = VisualTransformation.None,
            tint = Color.Black,
            contentColor = Color.Black
        )

        CustomOutlinedTextField(
            value = createEmail,
            thisValueChange = {
                createEmail = it
                updateButtonState(name, createEmail, createPassword, createPhoneNumber)
            },
            label = createEmailLabelMessage,
            modifier = Modifier
                .constrainAs(emailTF) {
                    top.linkTo(nameTF.bottom)
                }
                .fillMaxWidth()
                .padding(8.dp),
            imageVector = Icons.Default.Email,
            isError = isNewEmailError,
            visualTransformation = VisualTransformation.None,
            tint = Color.Black,
            contentColor = Color.Black
        )

        CustomOutlinedTextField(
            value = createPassword,
            thisValueChange = {
                createPassword = it
                updateButtonState(name, createEmail, createPassword, createPhoneNumber)
            },
            label = createPasswordLabelMessage,
            modifier = Modifier
                .constrainAs(passwordTF) {
                    top.linkTo(emailTF.bottom)
                }
                .fillMaxWidth()
                .padding(8.dp),
            imageVector = if (isCreatedPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            isError = isCreatePasswordError,
            isKeyboardPasswordType = true,
            isIconClickableParam = true,
            isPasswordToggleClicked = isCreatedPasswordVisible,
            isPasswordIconShowing = {
                isCreatedPasswordVisible = !isCreatedPasswordVisible
            },
            visualTransformation = if (isCreatedPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            tint = Color.Black,
            contentColor = Color.Black
        )

        CustomOutlinedTextField(
            value = createPhoneNumber,
            thisValueChange = {
                createPhoneNumber = it
                updateButtonState(name, createEmail, createPassword, createPhoneNumber)
            },
            label = createPhoneNumberLabelMessage,
            modifier = Modifier
                .constrainAs(phoneNumberTF) {
                    top.linkTo(passwordTF.bottom)
                }
                .fillMaxWidth()
                .padding(8.dp),
            imageVector = Icons.Filled.Phone,
            isError = phoneNumberError,
            visualTransformation = VisualTransformation.None,
            tint = Color.Black,
            contentColor = Color.Black,
            isKeyboardNumberType = true
        )

        CustomButton(
            text = stringResource(id = R.string.create_user),
            modifier = Modifier
                .constrainAs(signUpBtn) {
                    bottom.linkTo(parent.bottom)
                }
                .padding(8.dp),
            onButtonClick = {
                if (!userViewModel.validateEmail(createEmail)) {
                    isNewEmailError = true
                    createEmailLabelMessage = "Invalid username / email format"
                } else {
                    isNewEmailError = false
                    createEmailLabelMessage = createEmailText
                }

                if (!userViewModel.validatePassword(createPassword)) {
                    isCreatePasswordError = true
                    createPasswordLabelMessage = "Enter symbols of type format X, x, $ , 1"

                } else {
                    isCreatePasswordError = false
                    createPasswordLabelMessage = createdPasswordText
                }

                if (userViewModel.validateEmail(createEmail) && userViewModel.validatePassword(
                        createPassword
                    )
                ) {
                    val user = userViewModel.createUser(
                        name,
                        createEmail,
                        createPassword,
                        createPhoneNumber.toLong(),
                        profileImage = ""
                    )
                    coroutineScope.launch {
                        userViewModel.insertUser(user)
                    }
                    navController.navigate(Screen.Home.route)
                }
            },
            isEnabled = isButtonEnabled,
            fontSize = 20.sp,
            containerColor = Color.Gray,
            contentColor = Color.White,
            roundedShape = 8.dp,
            borderColor = Color.Gray,
        )
    }
}