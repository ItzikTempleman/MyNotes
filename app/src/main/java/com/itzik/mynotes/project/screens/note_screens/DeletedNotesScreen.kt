package com.itzik.mynotes.project.screens.note_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteForever
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun DeletedNotesScreen(
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
) {

    var noteList by remember { mutableStateOf(mutableListOf<Note>()) }

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
        val (titleLayout, lazyColumn) = createRefs()

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(titleLayout) {
                    top.linkTo(parent.top)
                }
                .background(Color.White)
        ) {
            val (returnBtn, titleText, trashBtn) = createRefs()

            IconButton(
                modifier = Modifier
                    .constrainAs(returnBtn) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(vertical = 8.dp),
                onClick = {
                    navController.navigate(Screen.Home.route)
                }
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    tint = Color.Black,
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier.constrainAs(titleText) {
                    start.linkTo(returnBtn.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = stringResource(id = R.string.deleted_notes),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )

            IconButton(
                modifier = Modifier
                    .constrainAs(trashBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
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
                    modifier = Modifier.size(26.dp),
                    tint = Color.Black,
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null
                )
            }
        }



        Column(
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    top.linkTo(titleLayout.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(noteList) { noteItem ->
                    NoteListItem(
                        isTrashed = true,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        note = noteItem,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.NoteScreen.route)
                        },
                        updatedList = {
                            noteList = it
                        }
                    )
                }
            }
        }
    }
}