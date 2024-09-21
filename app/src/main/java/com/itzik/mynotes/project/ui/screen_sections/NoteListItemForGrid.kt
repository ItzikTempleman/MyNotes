package com.itzik.mynotes.project.ui.screen_sections

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel

@Composable
fun NoteListItemForGrid(
    note: Note,
    modifier: Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    noteColor: Int = colorResource(id = R.color.intermediate_gray_4).toArgb(),
    noteViewModel: NoteViewModel,
    isSelected: Boolean,
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
    Box(
        modifier = modifier
            .height(180.dp)
            .padding(4.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(noteColor),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(noteColor, 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }

        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()
        ) {
            val (content, pinnedNoteIcon, likedNoteIcon) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(content) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .padding(8.dp),
                text = note.content,
                fontSize = 20.sp
            )

            if (isStarred) {
                Icon(
                    modifier = Modifier
                        .constrainAs(likedNoteIcon) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(38.dp)
                        .padding(end = 8.dp),
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colorResource(id = R.color.muted_yellow)
                )
            }

            if (isPinned) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    modifier = Modifier
                        .constrainAs(pinnedNoteIcon) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(end = 44.dp)
                        .size(30.dp)
                        .rotate(45f),
                    tint = colorResource(id = R.color.light_purple),
                    contentDescription = null
                )
            }
        }
    }
}

