package com.itzik.mynotes.project.ui.screens


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.model.LatLng
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.ui.composable_elements.EmptyStateMessage
import com.itzik.mynotes.project.ui.composable_elements.GenericIconButton
import com.itzik.mynotes.project.ui.composable_elements.SortDropDownMenu
import com.itzik.mynotes.project.ui.composable_elements.swipe_to_action.SwipeToOptions
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.screen_sections.NoteListItem
import com.itzik.mynotes.project.ui.screen_sections.NoteListItemForGrid
import com.itzik.mynotes.project.utils.TemperatureText
import com.itzik.mynotes.project.utils.convertFahrenheitToCelsius
import com.itzik.mynotes.project.utils.convertLatLangToLocation
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import com.itzik.mynotes.project.viewmodels.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private val permissions = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
)

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    userId: String,
    locationViewModel: LocationViewModel,
    context: Context,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteViewModel: NoteViewModel,
    currentLocation: LatLng,
    locationRequired: Boolean,
    startLocationUpdates: () -> Unit,
    updateIsLocationRequired: (Boolean) -> Unit,
    weatherViewModel: WeatherViewModel
) {

    LaunchedEffect(userId) {
        userViewModel.fetchUserById(userId)
        noteViewModel.updateUserIdForNewLogin()

    }

    var sortType by remember { mutableStateOf("") }
    var isLoadingLocation by remember { mutableStateOf(false) }
    var mutableLocationRequired by remember { mutableStateOf(locationRequired) }
    var locationName by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val noteList by noteViewModel.publicNoteList.collectAsState()
    val pinnedNoteList by noteViewModel.publicPinnedNoteList.collectAsState()
    val selectedNote by remember { mutableStateOf<Note?>(null) }
    var isViewGrid by remember { mutableStateOf(false) }
    val weatherItem by weatherViewModel.publicWeatherInstance.collectAsState()

    val combinedList by remember(pinnedNoteList, noteList) {
        mutableStateOf(
            (pinnedNoteList + noteList.filter { note ->
                !pinnedNoteList.contains(note) && !note.isInTrash && note.userId == userId
            }).distinctBy { it.noteId }
        )
    }

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

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            val (title, weatherIcon, temperature, sortOptionIcon, locationButton, newNoteBtn, viewBtn, noteLazyColumn, emptyStateMessage, progressBar) = createRefs()

            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            val firstWeatherOrNull = weatherItem.weather.firstOrNull()
            val painter = rememberImagePainter(data = firstWeatherOrNull?.getImage())


            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        CircleShape
                    )
                    .height(40.dp)
                    .width(40.dp)
                    .constrainAs(weatherIcon) {
                        start.linkTo(title.end)
                        top.linkTo(parent.top, margin = 6.dp)
                    },
                painter = painter,
                contentDescription = null
            )

            TemperatureText(
                modifier = Modifier
                    .constrainAs(temperature) {
                        start.linkTo(weatherIcon.end)
                        top.linkTo(parent.top)
                    }
                    .padding(12.dp),
                temperature = convertFahrenheitToCelsius(weatherItem.main.temp)

            )

            GenericIconButton(
                modifier = Modifier
                    .constrainAs(locationButton) {
                        end.linkTo(sortOptionIcon.start)
                        top.linkTo(parent.top)
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
                        if (locationName.isNotBlank()) {
                            isLoadingLocation = false
                            coroutineScope.launch {
                                weatherViewModel.getWeather(locationName)
                            }
                        }
                    } else {
                        launchMultiplePermissions.launch(permissions)
                    }
                },
                colorNumber = 4,
                imageVector = Icons.Outlined.LocationOn
            )

            Box(
                modifier = Modifier
                    .constrainAs(sortOptionIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(newNoteBtn.start)
                    }
                    .padding(8.dp)
            ) {
                GenericIconButton(
                    onClick = {
                        isExpanded = !isExpanded
                    },
                    colorNumber = 4,
                    imageVector = Icons.AutoMirrored.Filled.Sort
                )

                SortDropDownMenu(
                    isExpanded = isExpanded,
                    modifier = Modifier.wrapContentSize(),
                    coroutineScope = coroutineScope,
                    noteViewModel = noteViewModel,
                    onDismissRequest = {
                        isExpanded = false
                    },
                    updatedSortedList = {
                        sortType = it
                    }
                )
            }

            if (noteList.isEmpty()) {
                EmptyStateMessage(modifier = Modifier
                    .zIndex(4f)
                    .constrainAs(emptyStateMessage) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                    })
            }

            GenericIconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(newNoteBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                onClick = {
                    coroutineScope.launch {
                        noteViewModel.updateSelectedNoteContent(
                            "", isPinned = false, isStarred = false,
                            fontSize = 20, fontColor = Color.Black.toArgb(), userId = userId
                        )
                    }
                    navController.navigate(Screen.NoteScreen.route)
                }, imageVector = Icons.Outlined.Add,
                colorNumber = 4
            )

            GenericIconButton(
                modifier = Modifier
                    .constrainAs(viewBtn) {
                        end.linkTo(locationButton.start)
                        top.linkTo(parent.top)
                    }
                    .padding(8.dp),
                onClick = {
                    isViewGrid = !isViewGrid
                },
                imageVector = if (!isViewGrid) Icons.Default.GridView else Icons.Default.List,
                colorNumber = 4
            )

            if (!isViewGrid) {
                LazyColumn(
                    modifier = Modifier.constrainAs(noteLazyColumn) {
                        top.linkTo(viewBtn.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                ) {
                    items(combinedList, key = { it.noteId }) { noteItem ->

                        SwipeToOptions(
                            note = noteItem,
                            onPin = {
                                coroutineScope.launch {
                                    noteViewModel.togglePinButton(noteItem)
                                }
                            },
                            onStar = {
                                coroutineScope.launch {
                                    noteViewModel.toggleStarredButton(noteItem)
                                }
                            },
                            onDelete = {
                                coroutineScope.launch {
                                    noteViewModel.setTrash(noteItem)
                                }
                            }
                        ) {
                            NoteListItem(
                                isInHomeScreen = true,
                                noteViewModel = noteViewModel,
                                coroutineScope = coroutineScope,
                                note = noteItem,
                                modifier = Modifier
                                    .clickable {
                                        coroutineScope.launch {
                                            val noteId = noteItem.noteId
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                key = "noteId", value = noteId
                                            )
                                            noteViewModel.updateSelectedNoteContent(
                                                newChar = noteItem.content,
                                                noteId = noteItem.noteId,
                                                userId = noteItem.userId,
                                                isPinned = noteItem.isPinned,
                                                isStarred = noteItem.isStarred,
                                                fontSize = noteItem.fontSize,
                                                fontColor = noteItem.fontColor
                                            )
                                        }
                                        navController.navigate(Screen.NoteScreen.route)
                                    }
                                    .animateItemPlacement(),
                                updatedList = { updatedNotes ->
                                    noteViewModel.setNoteList(updatedNotes)
                                },
                                isSelected = selectedNote == noteItem,
                                isDeletedScreen = false,
                                isInLikedScreen = false
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.constrainAs(noteLazyColumn) {
                        top.linkTo(viewBtn.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                    columns = GridCells.Fixed(2),
                ) {
                    items(combinedList, key = { it.noteId }) { noteItem ->
                        NoteListItemForGrid(
                            modifier = Modifier.fillMaxSize(),
                            note = noteItem,
                            noteViewModel = noteViewModel,
                            isSelected = selectedNote == noteItem,
                        )

                    }
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
    }
}


