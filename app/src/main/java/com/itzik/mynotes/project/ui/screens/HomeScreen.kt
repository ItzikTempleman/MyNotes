package com.itzik.mynotes.project.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
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
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (title, sortOptionIcon, newNoteBtn, viewTypeBtn, noteLazyColumn, emptyStateMessage) = createRefs()

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
                        fontSize = 20, fontColor = Color.Black.toArgb(), userId = userId,
                    )
                }
                bottomBarNavController.navigate(Screen.NoteScreen.route)
            }, imageVector = Icons.Outlined.Add,
            colorNumber = 4
        )

        GenericIconButton(
            modifier = Modifier
                .constrainAs(viewTypeBtn) {
                    end.linkTo(sortOptionIcon.start)
                    top.linkTo(parent.top)
                }
                .padding(8.dp),
            onClick = {
                isViewGrid = !isViewGrid
                userViewModel.updateViewType(isViewGrid)
            },
            imageVector = if (!isViewGrid) Icons.Default.GridView else Icons.Default.List,
            colorNumber = 4
        )

        if (!isViewGrid) {
            LazyColumn(
                modifier = Modifier.constrainAs(noteLazyColumn) {
                    top.linkTo(viewTypeBtn.bottom)
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
                            note = noteItem,
                            modifier = Modifier
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
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier.constrainAs(noteLazyColumn) {
                    top.linkTo(viewTypeBtn.bottom)
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
    }
}