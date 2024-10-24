package com.itzik.mynotes.project.ui.composable_elements

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.itzik.mynotes.R

@Composable
fun EmailInputField(
    modifier: Modifier= Modifier.fillMaxWidth().padding(8.dp),
    email: String,
    onEmailChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    isError: Boolean = false,
) {
    CustomOutlinedTextField(
        modifier =modifier,
        value = email,
        rightImageVector = Icons.Rounded.Send,
        onValueChange = onEmailChanged,
        label = stringResource(R.string.your_email),
        isError = isError,
        leftImageVector = Icons.Default.Email,
        invokedFunction = onSendClick,
        keyboardType = KeyboardType.Email
    )
}

@Composable
fun SmsInputField(
    modifier: Modifier= Modifier.fillMaxWidth().padding(8.dp),
    code: String,
    onCodeFilled: (String) -> Unit,
    onSubmitCodeClick: () -> Unit,
    isError: Boolean = false,
) {
    CustomOutlinedTextField(
        modifier=modifier,
        value = code,
        onValueChange = onCodeFilled,
        label = stringResource(R.string.enter_code_sent_to_you),
        isError = isError,
        rightImageVector = Icons.Rounded.NavigateNext,
        leftImageVector = Icons.Default.Sms,
        invokedFunction = onSubmitCodeClick,
        keyboardType = KeyboardType.Text
    )
}

@Composable
fun NewPasswordInputField(
    modifier: Modifier= Modifier.fillMaxWidth().padding(8.dp),
    onToggleVisibility: () -> Unit,
    isPasswordVisible: Boolean,
    newPassword: String,
    onPasswordUpdated: (String) -> Unit,
    onConfirmPasswordClick: () -> Unit,
    isError: Boolean = false,
) {
    CustomOutlinedTextField(
        modifier = modifier,
        value = newPassword,
        isPasswordIconShowing = { onToggleVisibility() },
        onValueChange = onPasswordUpdated,
        label = stringResource(R.string.enter_new_password),
        rightImageVector = Icons.Default.ArrowCircleRight,
        isError = isError,
        invokedFunction = onConfirmPasswordClick,
        leftImageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
        keyboardType = KeyboardType.Password,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

sealed class ResetPasswordState {
    data object EnterEmail : ResetPasswordState()
    data object EnterCode : ResetPasswordState()
    data object EnterNewPassword : ResetPasswordState()
}