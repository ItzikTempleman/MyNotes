package com.itzik.mynotes.project.ui

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
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

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                coroutineScope.launch {
                    userViewModel.updateProfileImage(it.toString())
                }
            }
        }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (imageContainer, horizontalLine, verticalLine, uploadImageBtn, removeImageBtn, name, email, phone, bottomRow) = createRefs()


        Box(
            modifier = Modifier
                .constrainAs(imageContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(16.dp)
                .size(130.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (user.profileImage.isNotEmpty()) {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(model = user.profileImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .constrainAs(horizontalLine) {
                    top.linkTo(parent.top)
                }
                .padding(start = 8.dp, end = 8.dp, top = 50.dp),
            color = Color.Transparent
        )

        VerticalDivider(
            modifier = Modifier
                .constrainAs(verticalLine) {
                    start.linkTo(parent.start)
                }
                .padding(start = 140.dp),
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
                imagePickerLauncher.launch("image/*")
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
                .padding(start = 16.dp, top = 160.dp),
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

        LazyColumn(
            modifier = Modifier
                .constrainAs(bottomRow) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
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
