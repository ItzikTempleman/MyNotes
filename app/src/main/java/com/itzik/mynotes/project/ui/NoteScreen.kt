package com.itzik.mynotes.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NoteScreen(
    noteId: Int?,
    noteViewModel: NoteViewModel,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    paramNavController: NavHostController
) {
    val note by noteViewModel.publicNote.collectAsState()


    var text by remember {
        mutableStateOf(note.content)
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (returnIcon, title, pinIcon, starIcon, contentTF) = createRefs()

        IconButton(
            modifier = Modifier
                .constrainAs(returnIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(8.dp)
                .size(32.dp),
            onClick = {
                if (text.isNotEmpty()) {
                    coroutineScope.launch {
                        note.content = text
                        noteViewModel.saveNote(note)
                    }
                }
                paramNavController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = colorResource(id = R.color.blue_green)
            )
        }

        Icon(
            imageVector = Icons.Outlined.NoteAlt,
            contentDescription = null,
            tint = colorResource(id = R.color.blue_green),
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(returnIcon.end)
                }
        )

        if (note.isStarred) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = colorResource(id = R.color.light_yellow),
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
                    .constrainAs(starIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }

        if (note.isPinned) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = null,
                tint = colorResource(id = R.color.light_deep_purple),
                modifier = Modifier
                    .padding(end=38.dp, top=8.dp)
                    .size(20.dp)
                    .constrainAs(pinIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }



        TextField(
            value = text,
            onValueChange = {
                text = it
                coroutineScope.launch {
                    noteViewModel.updateSelectedNoteContent(
                        it,
                        noteId,
                        note.isPinned,
                        note.isStarred
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 0.dp, topStart = 0.dp))
                .constrainAs(contentTF) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
            textStyle = TextStyle.Default.copy(
                fontSize = 20.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            ),
            placeholder = {
                Text(
                    fontSize = 20.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    text = note.content.ifEmpty { stringResource(id = R.string.new_note) }
                )
            }
        )
    }
}