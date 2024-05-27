package com.itzik.mynotes.project.screens.note_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint( "MutableCollectionMutableState")
@Composable
fun DeletedNotesScreen(
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    modifier: Modifier
) {

    var noteList by remember { mutableStateOf(mutableListOf<Note>()) }

    LaunchedEffect(Unit){
        noteViewModel.fetchTrashedNotes().collect {
            noteList = it
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(noteList) { noteItem ->

                NoteListItem(
                    isTrashed=true,
                    noteViewModel=noteViewModel,
                    coroutineScope = coroutineScope,
                    note = noteItem,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.NoteScreen.route)
                    },
                    updatedList = {
                        noteList=it
                    }
                )
            }
        }
    }
}