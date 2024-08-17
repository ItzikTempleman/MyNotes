package com.itzik.mynotes.project.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun NoteOptionsLayout(
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    modifier: Modifier,
    note: Note,
    cancelOptionsFunction: () -> Unit,
) {

    val isPinned = remember { mutableStateOf(note.isPinned) }
    val isStarred = remember { mutableStateOf(note.isStarred) }

    val optionItems = listOf(
        NoteOptionsRows.PinNote(note, isPinned) {
            coroutineScope.launch {
                noteViewModel.togglePinButton(note)
                isPinned.value = !isPinned.value
                cancelOptionsFunction()
            }
        },
        NoteOptionsRows.DeletedNote(note) {
            cancelOptionsFunction()
        },
        NoteOptionsRows.StarNote(note, isStarred) {
            coroutineScope.launch {
                noteViewModel.toggleStarredButton(note)
                isStarred.value = !isStarred.value
                cancelOptionsFunction()
            }
        }
    )

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (topBorder, title, noteName, cancelOptions, lazyRow) = createRefs()

        HorizontalDivider(
            modifier = Modifier
                .constrainAs(topBorder) {
                    top.linkTo(parent.top)
                }
        )

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

        BoxWithConstraints(
            modifier = Modifier
                .constrainAs(noteName) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                }
                .padding(end = 140.dp, start = 40.dp)
        ) {
            val availableWidth = constraints.maxWidth * 2 / 3

            Text(
                maxLines = 1,
                modifier = Modifier.width(availableWidth.dp),
                text = note.content,
                fontSize = 20.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.SansSerif
            )
        }


        GenericIconButton(
            modifier = Modifier
                .padding(12.dp)
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
                NoteOptionItemComposable(
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
