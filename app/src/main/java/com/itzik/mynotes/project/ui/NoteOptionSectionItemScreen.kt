package com.itzik.mynotes.project.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun NoteOptionSectionItemScreen(
    modifier:Modifier,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteOptionsRows: NoteOptionsRows
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier.padding(8.dp)
            .size(100.dp)
            .clickable {
                noteOptionsRows.onClick(noteViewModel, coroutineScope, navController)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier
                    .padding(8.dp),
                imageVector = noteOptionsRows.icon, contentDescription = null, tint = Color.Gray,
            )

            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = noteOptionsRows.title,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun NoteOptionsLayout(
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    modifier: Modifier,
    note: Note
) {
    val optionItems = listOf(
        NoteOptionsRows.PinNote.apply { this.note = note },
        NoteOptionsRows.DeleterNote.apply { this.note = note },
        NoteOptionsRows.StarNote.apply { this.note = note }
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.cardColors()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                items(optionItems) { listItem ->
                    NoteOptionSectionItemScreen(
                        modifier=modifier,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        navController = navController,
                        noteOptionsRows = listItem
                    )
                }
            }
        }
    }
}