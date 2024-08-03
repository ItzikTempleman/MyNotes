package com.itzik.mynotes.project.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.ui.semantics.GenericIconButton
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
    val isPinned: Boolean = false,

    ) {
    class StarNote(note: Note, isStarred:Boolean,updatedList: ((MutableList<Note>) -> Unit)? = null) :
        NoteOptionsRows(
            title = "Star note",
            onClick = { currentNote, noteViewModel, _, _ ->
                noteViewModel.toggleLikedButton(currentNote)
            }, icon = if(isStarred) Icons.Default.Star else Icons.Outlined.StarOutline, updatedList = updatedList, isStarred = note.isLiked
        )

    class DeletedNote(note: Note, updatedList: ((MutableList<Note>) -> Unit)? = null) :
        NoteOptionsRows(
            title = "Delete note", onClick = { currentNote, noteViewModel, coroutineScope, _ ->
                coroutineScope.launch {
                    noteViewModel.setTrash(currentNote)
                    noteViewModel.publicNoteList.collect { collectedNotes ->
                        updatedList?.invoke(collectedNotes.toMutableList())
                    }
                }
            }, icon = Icons.Default.Delete, updatedList = updatedList
        )

    class PinNote(note: Note, isPinned:Boolean,updatedList: ((MutableList<Note>) -> Unit)? = null) : NoteOptionsRows(
        title = "Pin note", onClick = { currentNote, noteViewModel, coroutineScope, _ ->
            coroutineScope.launch {
                noteViewModel.togglePin(currentNote)
                updatedList?.invoke(noteViewModel.publicNoteList.value.toMutableList())
            }
        }, icon = if(isPinned) Icons.Default.PushPin else Icons.Outlined.PushPin, updatedList = updatedList, isPinned = note.isPinned
    )
}

@Composable
fun NoteOptionsLayout(
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    modifier: Modifier,
    note: Note,
    cancelOptionsFunction: ()->Unit
) {

    val optionItems = listOf(
        NoteOptionsRows.PinNote(note, note.isPinned) {
            coroutineScope.launch {
                noteViewModel.togglePin(note)
            }
        },
        NoteOptionsRows.DeletedNote(note),
        NoteOptionsRows.StarNote(note, note.isLiked) {
            coroutineScope.launch {
                noteViewModel.toggleLikedButton(note)
            }
        }
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(220.dp),
        colors = CardDefaults.cardColors(
            colorResource(id = R.color.very_light_gray)
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val (title, noteName, cancelOptions, lazyRow) = createRefs()

            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp, top = 12.dp
                    )
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                text = "Selected note ",
                fontSize = 24.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .padding(
                        start = 28.dp, top = 4.dp
                    )
                    .constrainAs(noteName) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                    },
                text = note.content,
                fontSize = 20.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )

            GenericIconButton(
                modifier = Modifier.padding(12.dp)
                .constrainAs(cancelOptions) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                onClick = {
                    cancelOptionsFunction()
                }, imageVector = Icons.Default.Cancel,
                colorNumber = 3
            )


            LazyRow(
                modifier = Modifier
                    .constrainAs(lazyRow) {
                        top.linkTo(noteName.bottom)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                items(optionItems) { listItem ->
                    NoteOptionSectionItemScreen(
                        modifier = modifier,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        navController = navController,
                        noteOptionsRows = listItem,
                        note = note
                    )
                }
            }
        }
    }
}

@Composable
fun NoteOptionSectionItemScreen(
    modifier: Modifier,
    note: Note,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteOptionsRows: NoteOptionsRows
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .width(125.dp)
            .aspectRatio(1f)
            .clickable {
                noteOptionsRows.onClick(
                    note,
                    noteViewModel,
                    coroutineScope,
                    navController
                )
            }, colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = if (noteOptionsRows.title == "Pin note") Modifier
                    .padding(6.dp)
                    .size(36.dp)
                    .rotate(45f) else Modifier
                    .padding(6.dp)
                    .size(36.dp),
                imageVector = noteOptionsRows.icon,
                contentDescription = null,
                tint = when (noteOptionsRows.title) {
                    "Star note" -> colorResource(id = R.color.light_yellow)
                    "Pin note" -> colorResource(id = R.color.light_deep_purple)
                    else -> colorResource(id = R.color.blue_green)
                }
            )
            Text(
                modifier = Modifier.padding(6.dp),
                text = noteOptionsRows.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

