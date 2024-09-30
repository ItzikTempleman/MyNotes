package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.screens.SelectedEditOption

sealed class EditProfileOptions(

    val itemName: String,
) {
    data object EditEmail : EditProfileOptions(
        itemName = "Email"

    )
    data object EditPhoneNumber : EditProfileOptions(
        itemName = "Phone number"
    )
}

@Composable
fun EditProfileItem(
    editProfileOptions: EditProfileOptions,
    modifier: Modifier,
    onItemSelected: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(36.dp).padding(8.dp)
                .clickable {
                    onItemSelected()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = editProfileOptions.itemName, fontSize = 12.sp
            )
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
    }
}



@Composable
fun EditProfileOptionsScreen(
    modifier: Modifier,
    onOptionSelected: (SelectedEditOption) -> Unit
) {
    val editOptionsRow = listOf(
        EditProfileOptions.EditEmail,
        EditProfileOptions.EditPhoneNumber
    )

    Column(
        modifier = modifier.width(120.dp).clip(RoundedCornerShape(8.dp)).background(colorResource(R.color.light_gray))
    ) {
        editOptionsRow.forEach {editOption->
            EditProfileItem(
                editProfileOptions = editOption,
                modifier = Modifier,
                onItemSelected = {
                    when(editOption){
                        is EditProfileOptions.EditEmail->onOptionSelected(SelectedEditOption.EMAIL)
                        is EditProfileOptions.EditPhoneNumber-> onOptionSelected(SelectedEditOption.PHONE_NUMBER)
                    }
                }
            )
        }
    }
}

