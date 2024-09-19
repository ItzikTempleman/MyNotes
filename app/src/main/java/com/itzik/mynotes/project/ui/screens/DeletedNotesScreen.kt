package com.itzik.mynotes.project.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.ui.composable_elements.EmptyStateMessage
import com.itzik.mynotes.project.ui.composable_elements.GenericItem
import com.itzik.mynotes.project.ui.composable_elements.GenericRows
import com.itzik.mynotes.project.ui.composable_elements.swipe_to_action.SwipeToDeleteOrRetrieve
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.screen_sections.NoteListItem
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
    rootNavController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel,
    userId:String

) {

    LaunchedEffect(Unit) {
        userViewModel.fetchUserById(userId)
        noteViewModel.fetchDeletedNotes()
    }
    val user by userViewModel.publicUser.collectAsState()

    val deleteNoteDialogItems = listOf(GenericRows.RetrieveNote, GenericRows.DeleteNote)

    val deletedNotes by noteViewModel.publicDeletedNoteList.collectAsState()

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    var isDeleteAllDialogOpen by remember {
        mutableStateOf(false)
    }

    val selectedNote by remember { mutableStateOf<Note?>(null) }
    var noteList by remember { mutableStateOf(deletedNotes) }



    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = modifier
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
                    rootNavController.navigate(Screen.Home.route)
                }

            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.Black
                )
            }


            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .constrainAs(trashBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .width(180.dp)
                    .height(70.dp)
                    .padding(8.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            isDeleteAllDialogOpen = !isDeleteAllDialogOpen
                        }
                ) {
                    Text(text = stringResource(id = R.string.empty_trash_bin))
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black,
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null
                    )
                }
            }

            if (isDeleteAllDialogOpen) {
                Button(
                    elevation = ButtonDefaults.buttonElevation(30.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.very_light_gray)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(deleteAllDialog) {
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(8.dp)
                        .zIndex(3f),
                    onClick = {
                        coroutineScope.launch {
                            noteViewModel.emptyTrashBin()
                        }
                        noteList = emptyList<Note>().toMutableList()
                        isDeleteAllDialogOpen = false
                    }
                ) {
                    Text(
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        text = "Delete all notes")
                }
            }

            if (deletedNotes.isEmpty()) {
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
                items(deletedNotes) { noteItem ->
                    SwipeToDeleteOrRetrieve(
                        note = selectedNote,
                        onRetrieve = {
                            coroutineScope.launch {
                                noteViewModel.retrieveNote(noteItem)
                                noteList = noteList.filter { it.noteId != noteItem.noteId }.toMutableList()
                            }
                        },
                        onDelete = {
                            coroutineScope.launch {
                                noteViewModel.deleteNotePermanently(noteItem)
                                noteList = noteList.filter { it.noteId != noteItem.noteId }.toMutableList()
                            }
                        }
                    ) {
                        NoteListItem(
                            isInHomeScreen = false,
                            noteViewModel = noteViewModel,
                            coroutineScope = coroutineScope,
                            note = noteItem,
                            modifier = Modifier, updatedList = {
                                noteList = it
                            },
                            isSelected = false,
                            isDeletedScreen = true,
                            isInLikedScreen = false
                        )
                    }
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
                            user?.let { user ->
                                GenericItem(
                                    modifier = modifier,
                                    item = it,
                                    noteViewModel = noteViewModel,
                                    coroutineScope = coroutineScope,
                                    navController = rootNavController,
                                    userViewModel = userViewModel,
                                    user = user
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


