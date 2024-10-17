package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
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
    isKeyboardNumberType: Boolean = false,
    isIconClickableParam: Boolean = false,
    visualTransformation: VisualTransformation,
    isPasswordIconShowing: ((Boolean) -> Unit)? = null,
    isPasswordToggleClicked: Boolean? = null,
) {
    val isIconClickableValue by remember {
        mutableStateOf(isIconClickableParam)
    }

    Column(modifier = modifier) {
        androidx.compose.material.OutlinedTextField(
            shape = MaterialTheme.shapes.small,
            value = value,
            onValueChange = { thisValueChange(it) },
            modifier = Modifier,
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
                            invokedFunction?.invoke()
                        }) {
                        trailingImage?.let {
                            Icon(
                                imageVector = it,
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
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorLabelColor = MaterialTheme.colorScheme.error
            ),
            isError = isError,
            keyboardOptions = when {
                isKeyboardPasswordType -> KeyboardOptions(keyboardType = KeyboardType.Password)
                isKeyboardNumberType -> KeyboardOptions(keyboardType = KeyboardType.Number)
                else -> KeyboardOptions(keyboardType = KeyboardType.Text)
            }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 12.dp),
            thickness = 1.dp
        )
    }
}

@Composable
fun EmptyStateMessage(
    modifier:Modifier,
    screenDescription:String?=""
) {
    Text(
        modifier = modifier, fontSize = 40.sp, color = Color.Gray, text = "No $screenDescription notes"
    )
}

