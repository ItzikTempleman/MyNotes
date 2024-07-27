package com.itzik.mynotes.project.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.semantics.SortDropDownMenu
import com.itzik.mynotes.project.utils.convertLatLangToLocation
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private val permissions = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
)


@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    locationViewModel: LocationViewModel,
    context: Context,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteViewModel: NoteViewModel,
    currentLocation: LatLng,
    locationRequired: Boolean,
    startLocationUpdates: () -> Unit,
    updateIsLocationRequired: (Boolean) -> Unit,

    ) {

    fun getSayHelloText(): String {
        return noteViewModel.sayHello()
    }

    var isLoadingLocation by remember {
        mutableStateOf(false)
    }

    var mutableLocationRequired by remember {
        mutableStateOf(locationRequired)
    }
    var locationName by remember {
        mutableStateOf("")
    }
    var isExpanded by remember { mutableStateOf(false) }
    val noteList by noteViewModel.publicNoteList.collectAsState()

    val launchMultiplePermissions =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
            val areGranted = it.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                mutableLocationRequired = true
                startLocationUpdates()
            }
            Toast.makeText(
                context,
                if (areGranted) "Permission Granted" else "Permission Declined",
                Toast.LENGTH_SHORT
            ).show()
            updateIsLocationRequired(mutableLocationRequired)
        }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (title, sortOptionIcon, sortDropDown, emptyStateMessage, noteLazyColumn, locationButton, newNoteBtn, progressBar) = createRefs()

        Icon(
            imageVector = Icons.Outlined.Home,
            contentDescription = null,
            tint = colorResource(id = R.color.deep_blue),
            modifier = Modifier
                .padding(top = 20.dp)
                .size(60.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        IconButton(
            modifier = Modifier
                .constrainAs(sortOptionIcon) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(8.dp),
            onClick = {
                isExpanded = !isExpanded
            }
        ) {
            Icon(imageVector = Icons.Default.Sort, contentDescription = null)
        }
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .constrainAs(sortDropDown) {
                        top.linkTo(sortOptionIcon.bottom)
                        end.linkTo(parent.end)
                    }
                    .background(Color.Gray)
                    .width(150.dp)
                    .height(100.dp)
                    .padding(8.dp)
            ) {
                SortDropDownMenu(
                    isExpandedParam = isExpanded,
                    modifier = Modifier.fillMaxSize(),
                    coroutineScope = coroutineScope,
                    navController = navController,
                    noteViewModel = noteViewModel
                )
            }
        }

        if (noteList.isEmpty()) {
            Text(
                modifier = Modifier.constrainAs(emptyStateMessage) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                }, fontSize = 40.sp, color = Color.Gray, text = "No notes"
            )
        }

        LazyColumn(modifier = Modifier
            .constrainAs(noteLazyColumn) {
                top.linkTo(title.bottom, margin = 16.dp)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }.padding(top=50.dp)
            .fillMaxWidth()
            .background(Color.White)) {
            items(noteList) { noteItem ->
                NoteListItem(isInHomeScreen = true,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope,
                    note = noteItem,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            val noteId = noteItem.id
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "noteId", value = noteId
                            )
                            noteViewModel.updateSelectedNoteContent(
                                noteItem.content,
                                noteId,
                                noteItem.isPinned,
                                noteItem.isLiked
                            )
                        }
                        navController.navigate(Screen.NoteScreen.route)
                    },
                    updatedList = { updatedNotes ->
                        noteViewModel.setNoteList(updatedNotes)
                    })
            }
        }

        FloatingActionButton(shape = CircleShape,
            containerColor = colorResource(id = R.color.deep_blue),
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(newNoteBtn) {
                    end.linkTo(parent.end)
                    bottom.linkTo(locationButton.top)
                },
            onClick = {
                coroutineScope.launch {
                    noteViewModel.updateSelectedNoteContent("", isPinned = false, isLiked = false)
                }
                navController.navigate(Screen.NoteScreen.route)
            }) {
            Icon(
                Icons.Filled.Add, null, tint = Color.White
            )
        }

        FloatingActionButton(shape = CircleShape,
            containerColor = Color.White,
            modifier = Modifier
                .constrainAs(locationButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(8.dp),
            onClick = {
                isLoadingLocation = true
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context, it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    startLocationUpdates()
                    locationName = convertLatLangToLocation(currentLocation, context)
                    locationViewModel.setLocationName(
                        convertLatLangToLocation(
                            currentLocation, context
                        )
                    )
                    if (locationName.isNotBlank()) isLoadingLocation = false
                } else {
                    launchMultiplePermissions.launch(permissions)
                }
            }) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = colorResource(
                    id = R.color.deep_blue
                )
            )
        }


        if (isLoadingLocation) {
            CircularProgressIndicator(modifier = Modifier.constrainAs(progressBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
        }
    }
}