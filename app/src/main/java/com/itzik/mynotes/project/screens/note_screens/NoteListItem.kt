package com.itzik.mynotes.project.screens.note_screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    updatedList: (MutableList<Note>) -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        val (deleteNote, likedNoteBtn, pinNote, content, verticalDiv, timeStamp, bottomLine) = createRefs()

        if (!isTrashed) {

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
                        noteViewModel.setTrash(note)
                        noteViewModel.publicNoteList.collect {
                            updatedList(it)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }


            IconButton(
                modifier = Modifier
                    .constrainAs(likedNoteBtn) {
                        end.linkTo(deleteNote.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                onClick = {
                    noteViewModel.toggleLikedButton(note)

                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = if (note.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.Red
                )
            }


            IconButton(
                modifier = Modifier
                    .graphicsLayer(rotationZ = 40f)
                    .constrainAs(
                        pinNote
                    ) {
                        end.linkTo(likedNoteBtn.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                onClick = {
                    noteViewModel.togglePinned(note)
                    //noteViewModel.updatePinnedNoteState(note)
                }
            ) {
                Icon(
                    modifier = Modifier.size(
                        20.dp
                    ),
                    imageVector = Icons.Default.PushPin,
                    contentDescription = null,
                    tint = if (note.isPinned) colorResource(id = R.color.navy_blue) else colorResource(
                        id = R.color.light_steel_blue
                    )
                )
            }

            Text(
                modifier = Modifier
                    .constrainAs(timeStamp) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }.padding(8.dp),
                text = note.time,
                fontSize = 12.sp
            )


            VerticalDivider(
                modifier = Modifier
                    .constrainAs(verticalDiv) {
                        start.linkTo(timeStamp.end)
                    }
                    .padding(vertical = 8.dp)
            )
        }



        Text(
            modifier = if(!isTrashed) Modifier
                .constrainAs(content) {
                    start.linkTo(timeStamp.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 16.dp) else Modifier
                .constrainAs(content) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 16.dp),
            text = note.content,
            fontSize = 16.sp
        )



        HorizontalDivider(
            modifier = Modifier
                .constrainAs(bottomLine) {
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 8.dp)
        )
    }
}
