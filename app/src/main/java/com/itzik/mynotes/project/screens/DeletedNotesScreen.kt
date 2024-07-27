package com.itzik.mynotes.project.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.IconButton
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
        val (returnIcon,title,trashBtn, lazyColumn) = createRefs()

        IconButton(
            modifier = Modifier
                .constrainAs(returnIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(start = 4.dp, top = 16.dp)
                .size(26.dp),
            onClick = {
                    navController.navigate(Screen.Home.route)
            }

        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null
            )
        }

    Icon(
            imageVector = Icons.Outlined.DeleteForever,
            contentDescription = null,
            tint = colorResource(id = R.color.navy_blue),
            modifier = Modifier.padding(top = 20.dp).size(60.dp).constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )


        IconButton(
                modifier = Modifier
                    .constrainAs(trashBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(vertical = 8.dp, horizontal = 20.dp),
                onClick = {
                    coroutineScope.launch {
                        noteViewModel.emptyTrashBin()
                    }
                    noteList = emptyList<Note>().toMutableList()
                }
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    tint = colorResource(id = R.color.navy_blue),
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null
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