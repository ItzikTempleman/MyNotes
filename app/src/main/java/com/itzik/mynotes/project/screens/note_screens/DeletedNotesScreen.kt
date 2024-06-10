package com.itzik.mynotes.project.screens.note_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
    modifier: Modifier
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
        val (titleLayout, deleteAllIcon, lazyColumn) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(titleLayout) {
                    top.linkTo(parent.top)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.very_light_deep_purple2),
                            Color.White
                        )
                    )
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                onClick = {
                    navController.navigate(Screen.Home.route)
                }) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    tint = colorResource(id = R.color.deep_purple),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null
                )
            }

            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(70.dp)
                    .padding(12.dp)
            ) {
                Row (
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Deleted notes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.deep_purple),
                    )
                }
            }
        }

        FloatingActionButton(
            shape = CircleShape,
            containerColor = Color.White,
            modifier = Modifier
                .constrainAs(deleteAllIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(8.dp)
                .size(50.dp),
            onClick = {
                coroutineScope.launch {
                    noteViewModel.emptyTrashBin()
//                    noteViewModel.fetchTrashedNotes().collect {
//                        noteList = it
//                    }
                }
                noteList = emptyList<Note>().toMutableList()
            }
        ) {
            Icon(
                tint = colorResource(id = R.color.deep_purple),
                imageVector = Icons.Default.DeleteForever,
                contentDescription = null
            )
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