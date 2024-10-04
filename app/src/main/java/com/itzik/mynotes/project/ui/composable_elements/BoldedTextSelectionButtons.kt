package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    textFieldValue: MutableState<TextFieldValue>,
    note: Note
) {
    val start = textFieldValue.value.selection.start
    val end = textFieldValue.value.selection.end
    val originalText = textFieldValue.value.annotatedString

    if (start != end) {
        val selectedAnnotatedString = buildAnnotatedString {
            append(originalText.substring(0, start))
            pushStyle(SpanStyle(fontWeight = if (isBolded) FontWeight.Bold else FontWeight.Normal))
            append(originalText.substring(start, end))
            pop()
            append(originalText.substring(end))
        }
        textFieldValue.value = textFieldValue.value.copy(
            annotatedString =  selectedAnnotatedString,
            selection = TextRange(end)
        )
    }else{
        val fullText = buildAnnotatedString {
            pushStyle(SpanStyle(fontWeight = if (isBolded) FontWeight.Bold else FontWeight.Normal))
            append(originalText.text)
            pop()
        }
        textFieldValue.value = textFieldValue.value.copy(
            annotatedString = fullText,
            selection = TextRange(end)
        )
    }

        Text(
            modifier = modifier
                .clickable {
                    coroutineScope.launch {
                        noteViewModel.updateSelectedNoteContent(
                            newChar = textFieldValue.value.annotatedString.text,
                            userId = note.userId,
                            noteId = note.noteId,
                            isPinned = note.isPinned,
                            isStarred = note.isStarred,
                            fontSize = note.fontSize,
                            fontColor = note.fontColor
                        )
                    }
                }
                .padding(4.dp),
            text = "B",
            fontSize = 20.sp, fontWeight = if (isBolded) FontWeight.Bold else FontWeight.Normal
        )
    }