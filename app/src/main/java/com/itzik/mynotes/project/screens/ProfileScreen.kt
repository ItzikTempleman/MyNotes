package com.itzik.mynotes.project.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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


@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun ProfileScreen(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.white),
                        Color.White
                    )
                )
            ),
    ) {
        val (title, dataContainer,deletedNotes, topDivider, settings,bottomDivider, signOut, bottomFixedDivider) = createRefs()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.blue_green),
                            Color.White
                        )
                    )
                )
        ) {

            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = null,
                        tint = colorResource(id = R.color.darker_blue),
                        modifier = Modifier.size(30.dp),
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Profile",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.darker_blue),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }


        Card(
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.semi_transparent_blue_green)),
            modifier = Modifier
                .constrainAs(dataContainer) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                }
                .fillMaxWidth()
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

                .constrainAs(deletedNotes) {
                    bottom.linkTo(topDivider.top)
                }
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = {
                navController.navigate(Screen.DeletedNotesScreen.route)
            },
            elevation = ButtonDefaults.elevation(defaultElevation = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    tint = Color.Black,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.deleted_notes),
                    color = Color.Black
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.constrainAs(topDivider){
                bottom.linkTo(settings.top)
            }
        )


        Button(
            modifier = Modifier

                .constrainAs(settings) {
                    bottom.linkTo(bottomDivider.top)
                }
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = {
                navController.navigate(Screen.Settings.route)
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
                    imageVector = Icons.Default.Settings,
                    tint = Color.Black,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.settings),
                    color = Color.Black
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.constrainAs(bottomDivider){
                bottom.linkTo(signOut.top)
            }
        )

        Button(
            modifier = Modifier
                .constrainAs(signOut) {
                    bottom.linkTo(bottomFixedDivider.top)
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

        HorizontalDivider(
            modifier = Modifier.constrainAs(bottomFixedDivider){
                bottom.linkTo(parent.bottom)
            }
        )
    }
}