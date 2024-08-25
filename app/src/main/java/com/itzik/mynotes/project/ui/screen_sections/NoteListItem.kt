package com.itzik.mynotes.project.ui.screen_sections

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun NoteListItem(
    isInHomeScreen: Boolean,
    isInLikedScreen: Boolean,
    modifier: Modifier,
    note: Note,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    updatedList: (MutableList<Note>) -> Unit,
    isSelected: Boolean,
    isDeletedScreen: Boolean
) {

    var isOptionVisible by remember {
        mutableStateOf(isSelected)
    }

    LaunchedEffect(isSelected) {
        isOptionVisible = isSelected
    }

    val pinStateMap by noteViewModel.publicPinStateMap.collectAsState()
    val starStateMap by noteViewModel.publicStarStateMap.collectAsState()

    val isPinned = pinStateMap[note.noteId] ?: false
    val isStarred = starStateMap[note.noteId] ?: false


    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        val (timeStamp, verticalDiv, content, bottomLine, pinnedNoteIcon, likedNoteIcon, optionIcon) = createRefs()


        BoxWithConstraints(modifier = if (isInHomeScreen) Modifier
            .constrainAs(content) {
                start.linkTo(verticalDiv.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
            .padding(end = 140.dp, start = 40.dp) else Modifier
            .constrainAs(content) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            .padding(8.dp)
        ) {
            val availableWidth = constraints.maxWidth * 2 / 3

            Text(
                maxLines = 1,
                modifier = Modifier.width(availableWidth.dp),
                text = note.content,
                fontSize = 20.sp,
                color = Color(note.fontColor),
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (isInLikedScreen) {
            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(bottomLine) {
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 8.dp),
                thickness = 1.dp,
            )
        }

        if (isInHomeScreen) {

            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(bottomLine) {
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 8.dp),
                thickness = 1.dp,
            )

            Text(
                modifier = Modifier
                    .constrainAs(timeStamp) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(8.dp),
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



            if (isStarred) {
                Icon(
                    modifier = Modifier
                        .constrainAs(likedNoteIcon) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(30.dp).padding(end=8.dp),
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colorResource(
                        id = R.color.muted_yellow
                    )
                )
            }

            if (isPinned) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    modifier = Modifier
                        .constrainAs(pinnedNoteIcon) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(end = 40.dp)
                        .size(24.dp)
                        .rotate(45f),
                    tint = colorResource(id = R.color.light_purple),
                    contentDescription = null
                )
            }

        }
    }
}
