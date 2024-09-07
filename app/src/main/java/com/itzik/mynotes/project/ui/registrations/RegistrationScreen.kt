package com.itzik.mynotes.project.ui.registrations


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
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
import com.itzik.mynotes.project.model.Gender
import com.itzik.mynotes.project.ui.composable_elements.CustomOutlinedTextField
import com.itzik.mynotes.project.ui.composable_elements.GenericDropDownMenu
import com.itzik.mynotes.project.ui.composable_elements.GenericIconButton
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel? = null
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
    var isExpanded by remember { mutableStateOf(false) }

    var isButtonEnabled by remember { mutableStateOf(false) }


    var isGenderExpanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf(Gender.MALE) }

    fun updateButtonState(name: String, email: String, password: String, phoneNumber: String) {
        if (userViewModel != null) {
            isButtonEnabled =
                userViewModel.validateName(name) &&
                        userViewModel.validateEmail(email) &&
                        userViewModel.validatePassword(password) &&
                        userViewModel.validatePhoneNumber(phoneNumber)
        }

    }



    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (backgroundBox, cardContainer, signUpBtn) = createRefs()

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


        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .constrainAs(cardContainer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(signUpBtn.top)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 12.dp)

        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {

                val (nameTF, emailTF, passwordTF, phoneNumberTF,genderSelector, selectGenderTitle, birthDate) = createRefs()

                CustomOutlinedTextField(
                    value = name,
                    thisValueChange = {
                        name = it
                        updateButtonState(name, createEmail, createPassword, createPhoneNumber)
                    },
                    label = nameLabelMessage,
                    modifier = Modifier
                        .constrainAs(nameTF) {
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .padding(8.dp),
                    imageVector = Icons.Default.Person,
                    isError = nameError,
                    visualTransformation = VisualTransformation.None,
                    tint = Color.Black,

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
                    isKeyboardNumberType = true
                )

                Box(
                    modifier = Modifier
                        .constrainAs(genderSelector) {
                            top.linkTo(phoneNumberTF.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(8.dp)
                ) {
                    GenericIconButton(
                        onClick = {
                            isExpanded = !isExpanded
                        },
                        colorNumber = 4,
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                    )

                    GenericDropDownMenu(
                        isGenderExpanded = isGenderExpanded,
                        modifier = Modifier.wrapContentSize(),
                        coroutineScope = coroutineScope,
                        onDismissRequest = {
                            isGenderExpanded = false
                        },
                        isSelectSort = false,
                        updatedList = {
                            selectedGender = when (it) {
                                "Male" -> Gender.MALE
                                "Female" -> Gender.FEMALE
                                "Other" -> Gender.OTHER
                                else -> Gender.MALE
                            }
                        }
                    )
                }
                Text(
                    modifier = Modifier.constrainAs(selectGenderTitle){
                        top.linkTo(phoneNumberTF.bottom)
                        start.linkTo(genderSelector.end)
                    } .padding(20.dp),
                    text= stringResource(R.string.select_gender),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }

        Button(
            modifier = Modifier
                .constrainAs(signUpBtn) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            onClick = {
                if (userViewModel != null) {
                    if (!userViewModel.validateEmail(createEmail)) {
                        isNewEmailError = true
                        createEmailLabelMessage = "Invalid username / email format"
                    } else {
                        isNewEmailError = false
                        createEmailLabelMessage = createEmailText
                    }
                }

                if (userViewModel != null) {
                    if (!userViewModel.validatePassword(createPassword)) {
                        isCreatePasswordError = true
                        createPasswordLabelMessage =
                            "Enter symbols of type format X, x, $ , 1"
                    } else {
                        isCreatePasswordError = false
                        createPasswordLabelMessage = createdPasswordText
                    }
                }

                if (userViewModel != null) {
                    if (userViewModel.validateEmail(createEmail) && userViewModel.validatePassword(
                            createPassword
                        )
                    ) {
                        val user = userViewModel.createUser(
                            name,
                            createEmail,
                            createPassword,
                            createPhoneNumber.toLong(),
                            profileImage = "",
                            gender = selectedGender,
                            dateOfBirth = ""
                        )
                        coroutineScope.launch {
                            try {
                                userViewModel.registerUser(user)
                                navController.navigate(Screen.Home.route)
                            } catch (e: Exception) {
                                Log.e(
                                    "RegistrationScreen",
                                    "Error registering user: ${e.message}"
                                )
                            }
                        }
                    }
                }
            },
            enabled = isButtonEnabled,
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.darker_blue)
            ),
        ) {
            Text(
                fontSize = 20.sp,
                text = stringResource(id = R.string.create_user)

            )
        }
    }
}


@Preview(showBackground = true, device = "spec:width=412dp,height=932dp")
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(
        coroutineScope = rememberCoroutineScope(),
        navController = rememberNavController()
    )
}
