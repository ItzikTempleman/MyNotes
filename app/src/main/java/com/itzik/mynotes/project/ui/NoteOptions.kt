package com.itzik.mynotes.project.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class NoteOptionsRows(
    val title: String,
    val modifier: Modifier = Modifier,
    var onClick: (note: Note, noteViewModel: NoteViewModel, coroutineScope: CoroutineScope, navController: NavHostController) -> Unit,
    val icon: ImageVector,
    val updatedList: ((MutableList<Note>) -> Unit)? = null,
    val isStarred: Boolean = false,
    val isPinned: Boolean = false
) {

    class StarNote(
        note: Note,
        isStarred: MutableState<Boolean>,
        updatedList: ((MutableList<Note>) -> Unit)? = null
    ) :
        NoteOptionsRows(
            title = "Star note",
            onClick = { currentNote, noteViewModel, coroutineScope, _ ->
                coroutineScope.launch {
                    noteViewModel.toggleStarredButton(currentNote)
                    isStarred.value = !isStarred.value
                }
            },
            icon = if (isStarred.value) Icons.Default.Star else Icons.Outlined.StarOutline,
            updatedList = updatedList,
            isStarred = isStarred.value
        )

    class DeletedNote(
        note: Note,
        updatedList: ((MutableList<Note>) -> Unit
        )? = null) :
    NoteOptionsRows(
    title = "Delete note", onClick =
    { currentNote, noteViewModel, coroutineScope, _ ->
        coroutineScope.launch {
            noteViewModel.setTrash(currentNote)
            noteViewModel.publicNoteList.collect { collectedNotes ->
                updatedList?.invoke(collectedNotes.toMutableList())
            }
        }
    }, icon = Icons.Default.Delete, updatedList = updatedList
    )

    class PinNote(
        note: Note,
        isPinned: MutableState<Boolean>,
        updatedList: ((MutableList<Note>) -> Unit
        )? = null
    ) : NoteOptionsRows(
        title = "Pin note",
        onClick = { currentNote, noteViewModel, coroutineScope, _ ->
            coroutineScope.launch {
                noteViewModel.togglePinButton(currentNote)
                isPinned.value=!isPinned.value
            }
        }, icon = if (isPinned.value)
            Icons.Default.PushPin else Icons.Outlined.PushPin,
        updatedList = updatedList,
        isPinned = isPinned.value
    )
}

