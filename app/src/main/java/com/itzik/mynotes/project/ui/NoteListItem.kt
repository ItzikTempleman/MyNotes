package com.itzik.mynotes.project.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
    modifier: Modifier,
    note: Note,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    updatedList: (MutableList<Note>) -> Unit,
    isOptionOpenMenu: (MutableState<Boolean>) -> Unit,
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

    val isPinned = pinStateMap[note.id] ?: false
    val isStarred = starStateMap[note.id] ?: false


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
                color = if (isInHomeScreen) Color(note.fontColor) else Color.DarkGray,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )
        }



        if (isInHomeScreen) {
            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(bottomLine) {
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 8.dp),
                thickness = 0.5.dp,
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

            GenericIconButton(
                modifier = Modifier
                    .constrainAs(optionIcon) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                onClick = {
                    isOptionVisible = !isOptionVisible
                    isOptionOpenMenu(mutableStateOf(isOptionVisible))
                },
                imageVector = Icons.Default.MoreVert,
                colorNumber = 3
            )

            if (isStarred) {
                Icon(
                    modifier = Modifier
                        .constrainAs(likedNoteIcon) {
                            end.linkTo(optionIcon.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(20.dp),
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colorResource(
                        id = R.color.light_yellow
                    )
                )
            }

            if (isPinned) {
                ConstraintLayout(
                    modifier = Modifier
                        .constrainAs(pinnedNoteIcon) {
                            end.linkTo(optionIcon.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(end = 30.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                            .constrainAs(createRef()) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Icon(
                        imageVector = Icons.Default.PushPin,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(45f),
                        tint = colorResource(id = R.color.light_deep_purple),
                        contentDescription = null
                    )
                }
            }

        }
    }
}
