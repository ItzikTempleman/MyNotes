package com.itzik.mynotes.project.screens.note_screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LikedNoteListItem(
    modifier: Modifier=Modifier,
    note: Note,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth().height(40.dp)
    ) {
        val (title, bottomLine)=createRefs()
        Text(
            modifier = Modifier.constrainAs(title){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
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