package com.itzik.mynotes.project.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.semantics.EmptyStateMessage
import com.itzik.mynotes.project.ui.semantics.GenericIconButton
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

@OptIn(ExperimentalFoundationApi::class)
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
    updateIsLocationRequired: (Boolean) -> Unit
) {

    var sortType by remember { mutableStateOf("") }
    var isLoadingLocation by remember { mutableStateOf(false) }
    var mutableLocationRequired by remember { mutableStateOf(locationRequired) }
    var locationName by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val noteList by noteViewModel.publicNoteList.collectAsState()
    val pinnedNoteList by noteViewModel.publicPinnedNoteList.collectAsState()
    var isOptionsBarOpened by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    val combinedList by remember(pinnedNoteList, noteList) {
        mutableStateOf(
            (pinnedNoteList + noteList.filter { note ->
                !pinnedNoteList.contains(note) && !note.isInTrash
            })
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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (
            title, sortOptionIcon, emptyStateMessage, noteLazyColumn,
            locationButton, newNoteBtn, progressBar, optionScreen
        ) = createRefs()

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
                    if (locationName.isNotBlank()) isLoadingLocation = false
                } else {
                    launchMultiplePermissions.launch(permissions)
                }
            },
            colorNumber = 3,
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
                colorNumber = 3,
                imageVector = Icons.Default.Sort
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
                        fontSize = 20, fontColor = Color.Gray.toArgb()
                    )
                }
                navController.navigate(Screen.NoteScreen.route)
            }, imageVector = Icons.Outlined.Add, colorNumber = 0
        )

        Card(
            modifier = modifier
                .padding(8.dp)
                .constrainAs(noteLazyColumn) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(combinedList, key = { it.id }) { noteItem ->
                    NoteListItem(
                        isInHomeScreen = true,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        note = noteItem,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    val noteId = noteItem.id
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        key = "noteId", value = noteId
                                    )
                                    noteViewModel.updateSelectedNoteContent(
                                        noteItem.content,
                                        noteId,
                                        noteItem.isPinned,
                                        noteItem.isStarred,
                                        noteItem.fontSize,
                                        noteItem.fontColor
                                    )
                                }
                                navController.navigate(Screen.NoteScreen.route)
                            }
                            .animateItemPlacement(),
                        updatedList = { updatedNotes ->
                            noteViewModel.setNoteList(updatedNotes)
                        },
                        isOptionOpenMenu = { isOpen ->
                            isOptionsBarOpened = isOpen.value
                            if (isOpen.value) {
                                selectedNote = noteItem
                            } else if (selectedNote == noteItem) {
                                selectedNote = null
                            }
                        },
                        isSelected = selectedNote == noteItem
                    )
                }
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

        if (isOptionsBarOpened && selectedNote != null) {
            NoteOptionsLayout(
                cancelOptionsFunction = {
                    isOptionsBarOpened = false
                    selectedNote = null
                },
                modifier = Modifier
                    .constrainAs(optionScreen) {
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .height(182.dp)
                    .background(Color.White),
                noteViewModel = noteViewModel,
                coroutineScope = coroutineScope,
                navController = navController,
                note = selectedNote!!
            )
        }
    }
}