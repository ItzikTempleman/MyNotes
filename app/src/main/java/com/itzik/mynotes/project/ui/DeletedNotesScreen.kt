package com.itzik.mynotes.project.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.ui.semantics.EmptyStateMessage
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun DeletedNotesScreen(
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController = rememberNavController()
) {

    var noteList by remember { mutableStateOf(mutableListOf<Note>()) }
    var isDialogOpen by remember {
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
            .fillMaxSize()
            .background(Color.White)

    ) {
        val (returnIcon, title, trashBtn, emptyStateMessage, lazyColumn, recoverDialog) = createRefs()

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
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = colorResource(id = R.color.blue_green)
            )
        }


        Icon(
            imageVector = Icons.Outlined.DeleteSweep,
            tint = colorResource(id = R.color.blue_green),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(returnIcon.end)
                }
        )


        IconButton(
            modifier = Modifier
                .constrainAs(trashBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(8.dp),
            onClick = {
                coroutineScope.launch {
                    noteViewModel.emptyTrashBin()
                }
                noteList = emptyList<Note>().toMutableList()
            }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                tint = colorResource(id = R.color.blue_green),
                imageVector = Icons.Default.DeleteForever,
                contentDescription = null
            )
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
                    top.linkTo(title.bottom, margin = 16.dp)
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
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .constrainAs(recoverDialog) {
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    modifier = Modifier,
                    onClick = {
                        coroutineScope.launch {

                        }
                    }
                ) {
                    Text(color = Color.White,text = "Retrieve ${selectedNote?.content}", fontSize = 24.sp)
                }

                Button(
                    modifier = Modifier,
                    onClick = {
                        coroutineScope.launch {

                        }
                    }
                ) {
                    Text(
                        color = Color.White,
                        text = "Delete Forever", fontSize = 24.sp)
                }
            }
        }
    }
}
