package com.itzik.mynotes.project.ui

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun ProfileScreen(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    noteViewModel: NoteViewModel,
    user: User,
) {


    var isEditClick by remember {
        mutableStateOf(false)
    }
    var isDoneButtonVisible by remember {
        mutableStateOf(false)
    }

    var selectedImageUri by remember {
        mutableStateOf(user.profileImage)
    }


    val profileItems = listOf(
        ProfileRows.DeletedItems, ProfileRows.Settings, ProfileRows.LogOut
    )

    val emptySateDrawable = R.drawable.baseline_person_24
    val loggedInUsers by userViewModel.exposedLoggedInUsersList.collectAsState(initial = emptyList())

    LaunchedEffect(loggedInUsers) {
        if (loggedInUsers.isNotEmpty()) {
            val loggedInUser = loggedInUsers.firstOrNull()
            selectedImageUri = loggedInUser?.profileImage ?: ""
        }

    }


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            coroutineScope.launch {
                selectedImageUri = uri.toString()
                userViewModel.updateProfileImage(selectedImageUri)
                val loggedInUser = loggedInUsers.firstOrNull()
                selectedImageUri = loggedInUser?.profileImage ?: ""
            }
            isDoneButtonVisible = true
        }
    )

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (imageContainer, name, editIcon, uploadImageButton, removePhotoText, done, email, phone, bottomItems) = createRefs()

        Image(
            painter = if (selectedImageUri != "") rememberAsyncImagePainter(model = selectedImageUri) else painterResource(
                id = emptySateDrawable
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(imageContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }.padding(20.dp)
                .size(80.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
        )

        Text(
            text = user.userName,
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(imageContainer.top)
                    bottom.linkTo(imageContainer.bottom)
                    start.linkTo(imageContainer.end)
                }
                .padding(start = 16.dp),
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            modifier = Modifier.constrainAs(editIcon) {
                top.linkTo(name.bottom)
                start.linkTo(name.start)
            },

            onClick = {
                isEditClick = true
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = null,
                tint = Color.Gray,
            )
        }

        if (isEditClick) {
            TextButton(
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .constrainAs(uploadImageButton) {
                        top.linkTo(name.bottom)
                        start.linkTo(editIcon.end)
                    }
                    .padding(start = 16.dp),
            ) {
                Text(
                    text = "Select photo",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            }
            if (!isDoneButtonVisible) {
            TextButton(
                onClick = {
                    selectedImageUri = ""
                    isDoneButtonVisible = true
                },
                modifier = Modifier
                    .constrainAs(removePhotoText) {
                        top.linkTo(name.bottom)
                        start.linkTo(uploadImageButton.end)
                    }
                    .padding(start = 16.dp),
            ) {
                Text(
                    text = "Remove photo",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            }
            }
        }

        if (isDoneButtonVisible) {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        userViewModel.updateProfileImage(selectedImageUri)
                    }
                    isDoneButtonVisible = false
                    isEditClick = false
                },
                modifier = Modifier
                    .constrainAs(done) {
                        top.linkTo(name.bottom)
                        start.linkTo(uploadImageButton.end)
                    }
                    .padding(start = 16.dp),
            ) {
                Text(
                    text = "Done",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .constrainAs(email) {
                    top.linkTo(imageContainer.bottom)
                }
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null,
                tint = Color.DarkGray
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.DarkGray,
                text = user.email
            )
        }

        Row(
            modifier = Modifier
                .constrainAs(phone) {
                    top.linkTo(email.bottom)
                }
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Call,
                contentDescription = null,
                tint = Color.DarkGray
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.DarkGray,
                text = user.phoneNumber.toString()
            )
        }


        LazyColumn(
            modifier = modifier
                .constrainAs(bottomItems) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
        ) {
            items(profileItems) {
                ProfileItem(
                    modifier = modifier,
                    profileItem = it,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope,
                    navController = navController,
                    userViewModel = userViewModel,
                    user = user
                )
            }
        }
    }
}