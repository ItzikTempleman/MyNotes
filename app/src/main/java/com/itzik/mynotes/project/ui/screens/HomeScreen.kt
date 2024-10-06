package com.itzik.mynotes.project.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.ui.composable_elements.EmptyStateMessage
import com.itzik.mynotes.project.ui.composable_elements.GenericIconButton
import com.itzik.mynotes.project.ui.composable_elements.SortDropDownMenu
import com.itzik.mynotes.project.ui.composable_elements.swipe_to_action.SwipeToOptions
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.screen_sections.NoteListItem
import com.itzik.mynotes.project.ui.screen_sections.NoteListItemForGrid
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    userId: String,
    coroutineScope: CoroutineScope,
    bottomBarNavController: NavHostController,
    noteViewModel: NoteViewModel
) {
    var sortType by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val noteList by noteViewModel.publicNoteList.collectAsState()
    val pinnedNoteList by noteViewModel.publicPinnedNoteList.collectAsState()
    val selectedNote by remember { mutableStateOf<Note?>(null) }
    var isViewGrid by remember { mutableStateOf(false) }
    val user by userViewModel.publicUser.collectAsState()
    var isImagePickerOpen by remember {
        mutableStateOf(false)
    }


    var imageSelected by remember {
        mutableStateOf("")
    }


    val combinedList by remember(pinnedNoteList, noteList) {
        mutableStateOf(
            (pinnedNoteList + noteList.filter { note ->
                !pinnedNoteList.contains(note) && !note.isInTrash && note.userId == userId
            }).distinctBy { it.noteId }
        )
    }


    LaunchedEffect(userId) {
        userViewModel.fetchUserById(userId)
        noteViewModel.updateUserIdForNewLogin()
        userViewModel.fetchViewType(userId)

    }

    LaunchedEffect(user) {
        isViewGrid = user?.isViewGrid ?: false
        imageSelected = user?.selectedWallpaper ?: ""
    }

    BackHandler {}

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (topRow, noteLazyColumn, emptyStateMessage) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(
                imageSelected
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Card(
            modifier = Modifier
                .constrainAs(topRow) {
                    top.linkTo(parent.top)
                }
                .padding(8.dp)
                .fillMaxWidth()
                .height(45.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(12.dp),
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                )

                IconButton(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(12.dp),

                    onClick = {
                        isImagePickerOpen = !isImagePickerOpen
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = null
                    )
                }

                VerticalDivider(modifier = Modifier.padding(12.dp))

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

                GenericIconButton(
                    modifier = Modifier
                        .padding(8.dp),
                    onClick = {
                        isViewGrid = !isViewGrid
                        userViewModel.updateViewType(isViewGrid)
                    },
                    imageVector = if (!isViewGrid) Icons.Default.GridView else Icons.Default.List,
                    colorNumber = 4
                )

                VerticalDivider(modifier = Modifier.padding(12.dp))

                GenericIconButton(
                    modifier = Modifier
                        .padding(8.dp),
                    onClick = {
                        coroutineScope.launch {
                            noteViewModel.updateSelectedNoteContent(
                                "",
                                isPinned = false,
                                isStarred = false,
                                fontSize = 20,
                                fontColor = Color.Black.toArgb(),
                                userId = userId,
                                fontWeight = 400
                            )
                        }
                        bottomBarNavController.navigate(Screen.NoteScreen.route)
                    }, imageVector = Icons.Outlined.Add,
                    colorNumber = 4
                )
            }
        }
        if (!isViewGrid) {
            LazyColumn(
                modifier = Modifier
                    .constrainAs(noteLazyColumn) {
                        top.linkTo(topRow.bottom)
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
                        }, content = {
                            NoteListItem(
                                isInHomeScreen = true,
                                noteViewModel = noteViewModel,
                                note = noteItem,
                                modifier = Modifier
                                    .background(colorResource(R.color.almost_transparent))
                                    .padding(horizontal = 8.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            val noteId = noteItem.noteId
                                            bottomBarNavController.currentBackStackEntry?.savedStateHandle?.set(
                                                key = "noteId", value = noteId
                                            )
                                            noteViewModel.updateSelectedNoteContent(
                                                newChar = noteItem.content,
                                                noteId = noteItem.noteId,
                                                userId = noteItem.userId,
                                                isPinned = noteItem.isPinned,
                                                isStarred = noteItem.isStarred,
                                                fontSize = noteItem.fontSize,
                                                fontColor = noteItem.fontColor,
                                                fontWeight = noteItem.fontWeight
                                            )
                                        }
                                        bottomBarNavController.navigate(Screen.NoteScreen.route)
                                    }
                                    .animateItemPlacement(),
                                updatedList = { updatedNotes ->
                                    noteViewModel.setNoteList(updatedNotes)
                                },
                                isSelected = selectedNote == noteItem,
                                isInLikedScreen = false
                            )
                        })
                }
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier.constrainAs(noteLazyColumn) {
                    top.linkTo(topRow.bottom)
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
        }
        if (isImagePickerOpen) {
            ImagesScreen(
                modifier = Modifier.fillMaxSize(),
                userViewModel = userViewModel,
                coroutineScope = coroutineScope,
                onImageSelected = { selectedImageUri ->
                    coroutineScope.launch {
                        if (selectedImageUri.isNotEmpty()) {
                            userViewModel.updateWallpaper(selectedImageUri)
                            isImagePickerOpen = false
                        } else {
                            return@launch
                        }
                    }
                },
                onScreenExit = {
                    isImagePickerOpen = false
                }
            )
        }

        if (combinedList.isEmpty()) {
            EmptyStateMessage(modifier = Modifier
                .zIndex(4f)
                .constrainAs(emptyStateMessage) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(topRow.bottom)
                })
        }
    }
}