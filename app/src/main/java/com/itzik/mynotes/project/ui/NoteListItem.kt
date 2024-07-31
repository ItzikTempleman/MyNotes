package com.itzik.mynotes.project.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
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
    isOptionOpenMenu: (MutableState<Boolean>) -> Unit
) {
    var isOptionVisible by remember {
        mutableStateOf(false)
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        val (timeStamp, verticalDiv, content, bottomLine,likedNoteBtn, optionIcon ) = createRefs()

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

        Text(
            modifier = Modifier
                .constrainAs(content) {
                    start.linkTo(verticalDiv.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 16.dp),
            text = note.content,
            fontSize = 20.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider(
            modifier = Modifier
                .constrainAs(bottomLine) {
                    bottom.linkTo(parent.bottom)
                }
                .padding(horizontal = 8.dp)
        )


        if (isInHomeScreen) {
            IconButton(
                modifier = Modifier
                    .constrainAs(optionIcon) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                onClick = {
                    isOptionVisible = !isOptionVisible
                    isOptionOpenMenu(mutableStateOf(isOptionVisible))
                }
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }

            if (note.isLiked) {
                Icon(
                    modifier = Modifier
                        .constrainAs(likedNoteBtn) {
                            end.linkTo(optionIcon.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }.size(36.dp),
                    imageVector =  Icons.Default.Star,
                    contentDescription = null,
                    tint = colorResource(
                        id = R.color.light_yellow
                    )
                )
            }
        }
    }
}
