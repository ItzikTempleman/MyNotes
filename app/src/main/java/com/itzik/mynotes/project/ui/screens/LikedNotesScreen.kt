package com.itzik.mynotes.project.ui.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.composable_elements.EmptyStateMessage
import com.itzik.mynotes.project.ui.composable_elements.swipe_to_action.SwipeToUnlike
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.screen_sections.NoteListItem
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "MutableCollectionMutableState")
@Composable
fun LikedNotesScreen(
    userId:String,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteViewModel: NoteViewModel
) {

    Log.d("TAG","User id from user id string: $userId ")
    LaunchedEffect(Unit) {
        noteViewModel.fetchStarredNotes()
    }

    val noteList by noteViewModel.publicStarredNoteList.collectAsState()


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)

        ) {
            val (title, emptyStateMessage, likedNotesLazyColumn) = createRefs()

            Icon(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(8.dp)
                    .size(32.dp),
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = colorResource(id = R.color.muted_yellow),
            )

            if (noteList.isEmpty()) {
                EmptyStateMessage(
                    screenDescription = "Starred",
                    modifier = Modifier
                        .zIndex(3f)
                        .constrainAs(emptyStateMessage) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            top.linkTo(parent.top)
                        })
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(likedNotesLazyColumn) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }

            ) {
                items(noteList) { noteItem ->
                    SwipeToUnlike(
                        note =noteItem,
                        onRemoveStar ={
                            coroutineScope.launch {
                                noteViewModel.unLikeNote(noteItem)
                            }
                        }
                    ) {
                        NoteListItem(
                            isInHomeScreen = false,
                            noteViewModel = noteViewModel,
                            coroutineScope = coroutineScope,
                            note = noteItem,
                            modifier = Modifier.clickable {
                                coroutineScope.launch {
                                    val noteId = noteItem.noteId
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        key = "noteId",
                                        value = noteId
                                    )
                                    noteViewModel.updateSelectedNoteContent(
                                        newChar = noteItem.content,
                                        noteId = noteId,
                                        isPinned = noteItem.isPinned,
                                        isStarred = noteItem.isStarred,
                                        fontSize = noteItem.fontSize,
                                        fontColor = noteItem.fontColor
                                    )
                                }
                                navController.navigate(Screen.NoteScreen.route)
                            },
                            updatedList = { updatedNotes ->
                                noteViewModel.setNoteList(updatedNotes)
                            },
                            isSelected = false,
                            isDeletedScreen = false,
                            isInLikedScreen = true
                        )
                    }
                }
            }
        }
    }
}



