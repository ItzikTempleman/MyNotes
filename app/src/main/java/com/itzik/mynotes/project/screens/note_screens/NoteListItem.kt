package com.itzik.mynotes.project.screens.note_screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NoteListItem(
    isTrashed:Boolean,
    modifier: Modifier,
    note: Note,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    updatedList: (MutableList<Note>) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (timeStamp, content, deleteNote, dividerLine) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(timeStamp) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(4.dp),
            text = note.date
        )

        Text(
            modifier = Modifier
                .constrainAs(content) {
                    start.linkTo(parent.start)
                    top.linkTo(timeStamp.bottom)
                }
                .padding(4.dp),
            text = note.content
        )

        if(!isTrashed) {
            IconButton(
                modifier = Modifier
                    .constrainAs(
                        deleteNote
                    ) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(4.dp),
                onClick = {
                    coroutineScope.launch {
                        noteViewModel.updateIsInTrashBin(note)
                        noteViewModel.fetchNotes().collect {
                            updatedList(it)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = colorResource(
                        id = R.color.blue_green
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .constrainAs(dividerLine) { top.linkTo(content.bottom) },
                thickness = 0.7.dp,
                color = Color.Gray
            )
        }
    }
}