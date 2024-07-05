package com.itzik.mynotes.project.screens.note_screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Row(
        modifier = modifier.padding(8.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = note.content,
            fontSize = 16.sp
        )
    }
}