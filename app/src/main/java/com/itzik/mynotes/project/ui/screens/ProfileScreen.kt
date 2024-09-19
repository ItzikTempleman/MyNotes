package com.itzik.mynotes.project.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PermContactCalendar
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.itzik.mynotes.R
import com.itzik.mynotes.project.main.NoteApp
import com.itzik.mynotes.project.model.Gender
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint(
    "CoroutineCreationDuringComposition", "MutableCollectionMutableState",
    "StateFlowValueCalledInComposition"
)
@Composable
fun ProfileScreen(
    coroutineScope: CoroutineScope,
    bottomBarNavController: NavHostController,
    rootNavController: NavHostController,
    userViewModel: UserViewModel,
    noteViewModel: NoteViewModel,
) {
    val user by userViewModel.publicUser.collectAsState()

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                NoteApp.instance.applicationContext.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
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
        val (imageContainer, horizontalLine, verticalLine, uploadImageBtn, removeImageBtn, name, email, phone, gender, age, bottomColumn) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(imageContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(16.dp)
                .size(130.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(0.3f)),
            contentAlignment = Alignment.Center
        ) {
            if (!user?.profileImage.isNullOrEmpty()) {

                Image(
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(user?.profileImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
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

        user?.let {
            Text(
                text = it.userName,
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
        }

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
            user?.let {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.DarkGray,
                    text = it.email
                )
            }
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
                text = user?.phoneNumber.toString()
            )
        }
        Row(
            modifier = Modifier
                .constrainAs(gender) {
                    top.linkTo(phone.bottom)
                }
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (user?.gender) {
                    Gender.MALE -> {
                        Icons.Default.Male
                    }

                    Gender.FEMALE -> {
                        Icons.Default.Female
                    }

                    Gender.OTHER -> {
                        Icons.Default.Transgender
                    }

                    null -> {
                        Icons.Default.Person
                    }
                },
                contentDescription = null,
                tint = Color.DarkGray
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.DarkGray,
                text = user?.gender.toString().lowercase()
            )
        }

        Row(
            modifier = Modifier
                .constrainAs(age) {
                    top.linkTo(gender.bottom)
                }
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.PermContactCalendar,
                contentDescription = null,
                tint = Color.DarkGray
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.DarkGray,
                text = "${user?.dateOfBirth} (age ${
                    user?.let {
                        userViewModel.getAgeFromSDateString(
                            it.dateOfBirth
                        )
                    }
                })"
            )
        }

        Column(
            modifier = Modifier
                .constrainAs(bottomColumn) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        bottomBarNavController.navigate(Screen.DeletedNotesScreen.route)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(4.dp),
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = null,
                    tint = Color.Black
                )
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = stringResource(R.string.trash_bin),
                    fontSize = 20.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
                thickness = 0.5.dp,
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch {
                            user?.isLoggedIn = false
                            user?.let { userViewModel.updateIsLoggedIn(it) }
                            noteViewModel.clearAllNoteList()
                            rootNavController.navigate(Screen.Login.route)
                        }
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(4.dp),
                    imageVector = Icons.Outlined.PowerSettingsNew,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = stringResource(R.string.log_out),
                    color = Color.Red,
                    fontSize = 20.sp
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                thickness = 0.75.dp
            )
        }

    }
}
