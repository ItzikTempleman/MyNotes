package com.itzik.mynotes.project.ui.composable_elements

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomOutlinedTextField(
    invokedFunction: (() -> Unit)? = null,
    tint: Color = Color.White,
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    label: String,
    modifier: Modifier = Modifier,
    leftImageVector: ImageVector,
    rightImageVector: ImageVector? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    isIconClickableParam: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isPasswordIconShowing: ((Boolean) -> Unit)? = null,
    isPasswordToggleClicked: Boolean? = null,
    isSingleLine:Boolean=true
) {
    val isIconClickableValue by remember {
        mutableStateOf(isIconClickableParam)
    }

    Column(modifier = modifier) {
       OutlinedTextField(
            shape = MaterialTheme.shapes.small,
            value = value,
            onValueChange = { onValueChange?.invoke(it) },
           modifier = Modifier.fillMaxWidth(),
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
                        imageVector = leftImageVector,
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
                            imageVector = leftImageVector,
                            contentDescription = null,
                            tint = tint
                        )
                    }
                }
            },
            trailingIcon = {
                if (rightImageVector!=null) {
                    IconButton(
                        onClick = {
                            invokedFunction?.invoke()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        rightImageVector?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                tint = Color.Black,
                            )
                        }
                    }
                }
            },
            singleLine = isSingleLine,
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
           keyboardOptions= KeyboardOptions(keyboardType = keyboardType)
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

