package com.itzik.mynotes.project.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
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
    user: User
) {
    val profileItems = listOf(GenericRows.DeletedItems, GenericRows.Settings, GenericRows.LogOut)


    val userList by userViewModel.publicLoggedInUsersList.collectAsState(initial = null)
    val loggedInUser = userList?.firstOrNull()
    var selectedImageUri by remember { mutableStateOf(user.profileImage) }
    LaunchedEffect(loggedInUser?.profileImage) {
        if (user.profileImage.isNotEmpty()) {
            selectedImageUri = user.profileImage
        }
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                coroutineScope.launch {
                    selectedImageUri = it.toString()
                    userViewModel.updateProfileImage(selectedImageUri)
                    Log.d("ProfileScreen", "Picked Image URI: $selectedImageUri")
                }
            }
        }
    )
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (imageContainer, profileCard, bottomRow) = createRefs()
        Box(
            modifier = Modifier
                .zIndex(2f)
                .constrainAs(imageContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(2.dp)
                .size(130.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(0.5.dp, Color.Black, CircleShape)
        ) {
            AsyncImage(
                model = selectedImageUri.ifEmpty { R.drawable.baseline_person_24 },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(R.drawable.ic_launcher_background)
            )
        }

        Card(
            modifier = Modifier
                .constrainAs(profileCard) {
                    top.linkTo(parent.top)
                }
                .padding(8.dp)
                .fillMaxWidth()
                .height(670.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    RoundedCornerShape(
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp,
                        topStart = 60.dp
                    )
                ),
            shape = RoundedCornerShape(
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp,
                topStart = 60.dp
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            ConstraintLayout(
                modifier = modifier.fillMaxSize()
            ) {
                val (horizontalLine, verticalLine, uploadImageBtn, removeImageBtn, name, email, phone) = createRefs()

                HorizontalDivider(
                    modifier = Modifier
                        .constrainAs(horizontalLine) {
                            top.linkTo(parent.top)
                        }
                        .padding(start = 8.dp, end = 8.dp, top = 40.dp),
                    color = Color.Transparent
                )

                VerticalDivider(
                    modifier = Modifier
                        .constrainAs(verticalLine) {
                            start.linkTo(parent.start)
                        }
                        .padding(start = 110.dp),
                    color = Color.Transparent
                )

                IconButton(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .constrainAs(uploadImageBtn) {
                            top.linkTo(horizontalLine.bottom)
                            start.linkTo(verticalLine.end)
                        },
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }

                IconButton(
                    modifier = Modifier
                        .constrainAs(removeImageBtn) {
                            top.linkTo(uploadImageBtn.top)
                            bottom.linkTo(uploadImageBtn.bottom)
                            start.linkTo(uploadImageBtn.end)
                        },
                    onClick = {
                        coroutineScope.launch {
                            userViewModel.updateProfileImage("")
                        }
                    }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }

                Text(
                    text = user.userName,
                    modifier = Modifier
                        .constrainAs(name) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .padding(start = 16.dp, top = 130.dp),
                    color = Color.Black,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .constrainAs(email) {
                            top.linkTo(name.bottom)
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

        Card(
            modifier = modifier
                .constrainAs(bottomRow) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            LazyColumn(
                modifier = modifier
            ) {
                items(profileItems) {
                    GenericItem(
                        user = user,
                        modifier = modifier,
                        item = it,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        navController = navController,
                        userViewModel = userViewModel,

                        )
                }
            }
        }
    }
}