package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BoldedTextSelectionButtons(
    modifier: Modifier,
    isBolded: Boolean,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    note: Note,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope
) {
    val start = textFieldValue.selection.start
    val end = textFieldValue.selection.end
    val originalText = textFieldValue.annotatedString

    val updatedAnnotatedString = buildAnnotatedString {
        if (start != end) {
            append(originalText.substring(0, start))
            pushStyle(SpanStyle(fontWeight = if (isBolded) FontWeight.Bold else FontWeight.Normal))
            append(originalText.substring(start, end))
            pop()
            append(originalText.substring(end))
        } else {
            pushStyle(SpanStyle(fontWeight = if (isBolded) FontWeight.Bold else FontWeight.Normal))
            append(originalText.text)
        }
    }

    val newTextFieldValue = textFieldValue.copy(
        annotatedString = updatedAnnotatedString,
        selection = TextRange(end)
    )

    Text(
        modifier = modifier
            .clickable {
                onValueChange(newTextFieldValue)
                coroutineScope.launch {

                    noteViewModel.updateSelectedNoteContent(
                        newChar = newTextFieldValue.text,
                        noteId = note.noteId,
                        isPinned = note.isPinned,
                        isStarred = note.isStarred,
                        fontSize = note.fontSize,
                        fontColor = note.fontColor,
                        userId = note.userId,
                        fontWeight = noteViewModel.fontWeightToInt(if (isBolded) FontWeight.Bold else FontWeight.Normal)
                    )
                }
            }
            .padding(4.dp),
        text = "B",
        fontSize = 20.sp, fontWeight = if (isBolded) FontWeight.Bold else FontWeight.Normal
    )
}