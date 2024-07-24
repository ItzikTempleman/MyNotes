package com.itzik.mynotes.project.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.note_screens.LikedNoteListItem
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint("SuspiciousIndentation")
@Composable
fun LikedNotesScreen(
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User,
    noteViewModel: NoteViewModel
) {


    var noteList by remember { mutableStateOf(mutableListOf<Note>()) }

    LaunchedEffect(Unit) {
        noteViewModel.fetchLikedNotes().collect {
            noteList = it
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()

    ) {

        val (title, likedNotesLazyColumn) = createRefs()

                    Icon(
                        modifier = Modifier
                            .constrainAs(title) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }.padding( top = 20.dp).size(60.dp),
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = colorResource(id = R.color.light_yellow),
                    )




        LazyColumn(
            modifier = Modifier
                .constrainAs(likedNotesLazyColumn) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
        ) {
            items(noteList) {
                LikedNoteListItem(
                    note = it,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope
                )
            }
        }
    }
}



