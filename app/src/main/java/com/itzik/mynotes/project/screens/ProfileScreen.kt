package com.itzik.mynotes.project.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileScreen(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User,
) {

    Log.d("TAG", "user: $user")
    var isEditClick by remember {
        mutableStateOf(false)
    }
    var isDoneButtonVisible by remember {
        mutableStateOf(false)
    }

    var selectedImageUri by remember {
        mutableStateOf(user.profileImage)
    }

    val emptySateDrawable = R.drawable.baseline_person_24

    LaunchedEffect(Unit) {
        if (selectedImageUri.isNotEmpty()) {
            userViewModel.fetchLoggedInUsers().collect {
                if (it.isNotEmpty()) {
                    selectedImageUri = it.first().profileImage
                }
            }
        }
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            coroutineScope.launch {
                selectedImageUri = uri.toString()
                userViewModel.updateProfileImage(selectedImageUri)
                userViewModel.fetchLoggedInUsers().collect {
                    selectedImageUri = it.first().profileImage
                }
            }
            isDoneButtonVisible = true
        }
    )

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.blue_green),
                        Color.White
                    )
                )
            ),
    ) {
        val (dataContainer, signOut) = createRefs()

        Card(
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .constrainAs(dataContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .width(360.dp)
                .height(360.dp)
                .padding(12.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                val (imageContainer, name, editIcon, uploadImageButton, removePhotoText, done, email, phone) = createRefs()

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
                        }
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

                    TextButton(
                        onClick = {
                            selectedImageUri = ""
                            isDoneButtonVisible = true
                        },
                        modifier = Modifier
                            .constrainAs(removePhotoText) {
                                top.linkTo(uploadImageButton.bottom)
                                start.linkTo(uploadImageButton.start)
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
                            .padding(start = 8.dp),
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
            }
        }

        Button(
            modifier = Modifier
                .border(
                    border =
                    BorderStroke(width = 0.1.dp, color = Color.LightGray)
                )
                .constrainAs(signOut) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = {
                coroutineScope.launch {
                    user.isLoggedIn = false
                    userViewModel.updateIsLoggedIn(user)
                }
                navController.navigate(Screen.Login.route)
            },
            elevation = ButtonDefaults.elevation(
                defaultElevation = 12.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    tint = Color.Red,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.log_out),
                    color = Color.Red
                )
            }
        }
    }
}