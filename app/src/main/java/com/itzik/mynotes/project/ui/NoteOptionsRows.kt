package com.itzik.mynotes.project.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.ui.NoteOptionsRows.DeleterNote.isStarred
import com.itzik.mynotes.project.ui.NoteOptionsRows.DeleterNote.note
import com.itzik.mynotes.project.ui.NoteOptionsRows.DeleterNote.updatedList
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class NoteOptionsRows(
    val title: String,
    val modifier: Modifier=Modifier,
    var onClick: (noteViewModel: NoteViewModel, coroutineScope: CoroutineScope, navController: NavHostController) -> Unit,
    val icon: ImageVector,
    var note: Note? = null,
    val updatedList: ((MutableList<Note>) -> Unit)? = null,
    var isStarred: Boolean = false,
    val isPinned: Boolean = false,
) {

    data object StarNote : NoteOptionsRows(
        title = "Star note",
        onClick = { noteViewModel, _, _->
            isStarred=!isStarred
            note?.let { noteViewModel.toggleLikedButton(it) }

        },
        icon =  Icons.Default.Star,

    )


    data object DeleterNote : NoteOptionsRows(
        title = "Delete note",
        onClick = { noteViewModel, coroutineScope, _ ->
            coroutineScope.launch {
                    note?.let { noteViewModel.setTrash(it) }

                noteViewModel.publicNoteList.collect {
                    updatedList?.let { it1 -> it1(it) }
                }
            }
        },
        icon = Icons.Default.Delete,

    )

    data object PinNote : NoteOptionsRows(
        title = "Pin note",
        onClick = { noteViewModel, coroutineScope, _->
            coroutineScope.launch {
                note?.let { noteViewModel.togglePin(it) }
            }
        },
        icon = Icons.Default.PushPin,

    )
}

