package com.itzik.mynotes.project.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.note_screens.LikedNoteListItem
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LikedNotesScreen(
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User,
    noteViewModel: NoteViewModel
) {
    val likedNoteList by noteViewModel.publicLikedNoteList.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()

    ) {

        val (title, likedNotesLazyColumn) = createRefs()
        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(top = 12.dp, start = 16.dp),
            text = stringResource(id = R.string.liked_notes),
            fontSize = 24.sp,
            color = colorResource(id = R.color.darker_blue),
            fontWeight = FontWeight.Bold
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
            items(likedNoteList) {
                LikedNoteListItem(
                    note = it,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope
                )
            }
        }
    }
}



