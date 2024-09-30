package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        itemName = "Change email",
        onItemSelected = { userViewModel, user, coroutineScope ->
            coroutineScope.launch {
                userViewModel.updateUser(user.email)
            }
        }
    )

    data object EditPassword : EditProfileOptions(
        itemName = "Change password",
        onItemSelected = { userViewModel, user, coroutineScope ->
            coroutineScope.launch {
                userViewModel.updateUser(user.password)
            }

        }
    )

    data object EditPhoneNumber : EditProfileOptions(
        itemName = "Change phone number",
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
    //isFieldEditable:(MutableState<Boolean>)->Unit
) {
    var mutableStateOfIsFieldEditable by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                editProfileOptions.onItemSelected
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            tint = Color.Red
        )

        Text(
            modifier = Modifier.padding(4.dp),
            text = editProfileOptions.itemName
        )
    }
}


@Composable
fun EditProfileOptionsScreen(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    userViewModel: UserViewModel,
    user: User,
    isEditProfileOptionListVisible: (Boolean) -> Unit
) {

    val editOptionsRow = listOf(
        EditProfileOptions.EditEmail,
        EditProfileOptions.EditPassword,
        EditProfileOptions.EditPhoneNumber
    )

    LazyColumn(
        modifier = modifier.width(150.dp)
    ) {
        items(editOptionsRow) {
            EditProfileItem(
                editProfileOptions = it,
                modifier = modifier
            )
        }
    }

}