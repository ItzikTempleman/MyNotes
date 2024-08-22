package com.itzik.mynotes.project.ui


import android.annotation.SuppressLint
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "MutableCollectionMutableState")
@Composable
fun LikedNotesScreen(
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User,
    modifier: Modifier,
    noteViewModel: NoteViewModel
) {


    var noteList by remember { mutableStateOf(mutableListOf<Note>()) }

    LaunchedEffect(Unit) {
        noteViewModel.fetchStarredNotes().collect {
            noteList = it
        }
    }

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
            tint = colorResource(id = R.color.light_yellow),
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
                NoteListItem(
                    isInHomeScreen = false,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope,
                    note = noteItem,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            val noteId = noteItem.id
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




