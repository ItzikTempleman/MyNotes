package com.itzik.mynotes.project.screens.sections

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.itzik.mynotes.R


@Composable
fun CustomOutlinedTextField(
    invokedFunction: (() -> Unit)? = null,
    tint: Color,
    isTrailingIconExist: Boolean = false,
    value: String,
    thisValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier,
    imageVector: ImageVector,
    trailingImage: ImageVector? = null,
    isError: Boolean = false,
    isKeyboardPasswordType: Boolean = false,
    isKeyboardNumberType : Boolean =false,
    isIconClickableParam: Boolean = false,
    visualTransformation: VisualTransformation,
    isPasswordIconShowing: ((Boolean) -> Unit)? = null,
    isPasswordToggleClicked: Boolean? = null,
   contentColor:Color
) {

    val isIconClickableValue by remember {
        mutableStateOf(isIconClickableParam)
    }

        OutlinedTextField(

            value = value,
            onValueChange = {
                thisValueChange(it)
            },
            modifier = modifier,
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                )
            },
            leadingIcon = {
                if (!isIconClickableValue) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = tint
                    )
                } else {
                    IconButton(
                        onClick = {
                            if (isPasswordIconShowing != null) {
                                isPasswordToggleClicked?.let {
                                    isPasswordIconShowing(it)
                                }
                            }
                        }) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = null,
                            tint = tint
                        )
                    }
                }
            },
            trailingIcon = {
                if (isTrailingIconExist) {
                    IconButton(
                        onClick = {
                            if (invokedFunction != null) {
                                invokedFunction()
                            }
                        }) {
                        if (trailingImage != null) {
                            Icon(
                                imageVector = trailingImage,
                                contentDescription = null,
                                tint = Color.Black,
                            )
                        }
                    }
                }
            },
            singleLine = true,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.darker_blue),
                cursorColor = colorResource(id = R.color.darker_blue)
            ),
            isError = isError,
            keyboardOptions = if (isKeyboardPasswordType) {
                KeyboardOptions(keyboardType = KeyboardType.Password)
            } else if(isKeyboardNumberType) {
                KeyboardOptions(keyboardType = KeyboardType.Number)
            } else{
                KeyboardOptions(keyboardType = KeyboardType.Text)
            }

        )
    }




