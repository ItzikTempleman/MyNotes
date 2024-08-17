package com.itzik.mynotes.project.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.semantics.EmptyStateMessage
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun DeletedNotesScreen(
    modifier: Modifier,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController = rememberNavController(),
    user: User,
    userViewModel: UserViewModel

) {

    val deleteNoteDialogItems = listOf(GenericRows.RetrieveNote, GenericRows.DeleteNote)

    var noteList by remember { mutableStateOf(mutableListOf<Note>()) }
    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    var isDeleteAllDialogOpen by remember {
        mutableStateOf(false)
    }

    var selectedNote by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(Unit) {
        noteViewModel.fetchTrashedNotes().collect {
            noteList = it
        }
    }


    ConstraintLayout(
        modifier = Modifier
            .clickable {
                isDeleteAllDialogOpen = false
                isDialogOpen = false
            }
            .fillMaxSize()
            .background(Color.White)

    ) {
        val (returnIcon, trashBtn, deleteAllDialog, emptyStateMessage, lazyColumn, recoverDialog) = createRefs()

        IconButton(
            modifier = Modifier
                .constrainAs(returnIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(8.dp),
            onClick = {
                navController.navigate(Screen.Home.route)
            }

        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = Color.Black
            )
        }

        IconButton(
            modifier = Modifier
                .constrainAs(trashBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(8.dp),
            onClick = {
                isDeleteAllDialogOpen = !isDeleteAllDialogOpen
            }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                tint = Color.Black,
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
        if (isDeleteAllDialogOpen) {
            Card(
                modifier = Modifier
                    .constrainAs(deleteAllDialog) {
                        top.linkTo(parent.top)
                        end.linkTo(trashBtn.start)
                    }
                    .clickable {
                        coroutineScope.launch {
                            noteViewModel.emptyTrashBin()
                        }
                        noteList = emptyList<Note>().toMutableList()
                    }
                    .width(230.dp)
                    .padding(24.dp)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Default.DeleteForever,
                        tint = Color.Red,
                        contentDescription = null
                    )
                    Text(text = "Delete all notes")
                }
            }
        }


        if (noteList.isEmpty()) {
            EmptyStateMessage(
                screenDescription = "Deleted",
                modifier = Modifier.constrainAs(emptyStateMessage) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    top.linkTo(returnIcon.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .background(Color.White),
        ) {
            items(noteList) { noteItem ->
                NoteListItem(
                    isInHomeScreen = false,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope,
                    note = noteItem,
                    modifier = Modifier.clickable {
                        selectedNote = noteItem
                        isDialogOpen = !isDialogOpen
                    },
                    updatedList = {
                        noteList = it
                    },
                    isOptionOpenMenu = {

                    },
                    isSelected = false
                )
            }
        }
        if (isDialogOpen) {
            Card(
                modifier = Modifier
                    .constrainAs(recoverDialog) {
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .padding(24.dp)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                LazyColumn(
                    modifier = modifier
                ) {
                    items(deleteNoteDialogItems) {
                        GenericItem(
                            modifier = modifier,
                            item = it,
                            noteViewModel = noteViewModel,
                            coroutineScope = coroutineScope,
                            navController = navController,
                            userViewModel = userViewModel,
                            user = user
                        )
                    }
                }
            }
        }
    }
}


