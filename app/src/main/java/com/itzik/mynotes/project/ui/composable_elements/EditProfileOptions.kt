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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class EditProfileOptions(
    val onItemSelected: (
        userViewModel: UserViewModel,
        user: User,
        coroutineScope: CoroutineScope
    ) -> Unit,
    val itemName: String
) {

    data object EditEmail : EditProfileOptions(
        itemName = "Email",
        onItemSelected = { userViewModel, user, coroutineScope ->
            coroutineScope.launch {
                userViewModel.updateUser(user.email)
            }
        }
    )

    data object EditPassword : EditProfileOptions(
        itemName = "Password",
        onItemSelected = { userViewModel, user, coroutineScope ->
            coroutineScope.launch {
                userViewModel.updateUser(user.password)
            }

        }
    )

    data object EditPhoneNumber : EditProfileOptions(
        itemName = "Phone number",
        onItemSelected = { userViewModel, user, coroutineScope ->
            coroutineScope.launch {
                userViewModel.updateUser(user.phoneNumber)
            }
        }
    )
}

@Composable
fun EditProfileItem(
    editProfileOptions: EditProfileOptions,
    modifier: Modifier,
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
                    editProfileOptions.onItemSelected
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
    isEditProfileOptionListVisible: (Boolean) -> Unit
) {

    val editOptionsRow = listOf(
        EditProfileOptions.EditEmail,
        EditProfileOptions.EditPassword,
        EditProfileOptions.EditPhoneNumber
    )

    LazyColumn(
        modifier = modifier.width(120.dp).clip(RoundedCornerShape(8.dp)).background(colorResource(R.color.light_gray))
    ) {
        items(editOptionsRow) {
            EditProfileItem(
                editProfileOptions = it,
                modifier = modifier
            )
        }
    }
}