package com.itzik.mynotes.project.screens.note_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NoteListItem(
    isTrashed: Boolean,
    modifier: Modifier,
    note: Note,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    updatedList: (MutableList<Note>) -> Unit,
) {
    Card(
        colors=CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = modifier
            .padding(8.dp)
            .wrapContentSize()
    ) {
        ConstraintLayout(
            modifier = if(!isTrashed) Modifier.fillMaxSize().background(Color.White) else Modifier.height(70.dp).fillMaxWidth().background(Color.White)
        ) {
            val ( timeStamp, deleteNote, content) = createRefs()

            if (!isTrashed) {
            Text(
                modifier = Modifier
                    .constrainAs(timeStamp) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(vertical = 18.dp, horizontal = 50.dp),
                text = note.time,
                fontSize = 12.sp
            )

                IconButton(
                    modifier = Modifier
                        .constrainAs(
                            deleteNote
                        ) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
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
                            id = R.color.deep_purple
                        )
                    )
                }
            }
            Text(
                modifier = Modifier
                    .constrainAs(content) {
                        start.linkTo(parent.start)
                        top.linkTo(timeStamp.bottom)
                    }
                    .padding(4.dp),
                text = note.content,
                fontSize = 20.sp
            )

        }
    }
}