package com.itzik.mynotes.project.screens.note_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//TODO THE ISSUE IS THAT NOTE_SCREEN IS A DIFFERENT NAV_CONTROLLER!!!!!

@SuppressLint("SuspiciousIndentation")
@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    paramNavController: NavHostController,
) {

    val savedStateHandle = paramNavController.currentBackStackEntry?.savedStateHandle
    val note = savedStateHandle?.get<Note>("note") ?: Note(content = "")

    var isLayoutText by remember {
        mutableStateOf(false)
    }

    var text by remember {
        mutableStateOf(note.content)
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()

    ) {
        val (returnIcon, icon, doneBtn, contentTF) = createRefs()

        IconButton(
            enabled = isLayoutText,
            modifier = Modifier
                .constrainAs(returnIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(start = 4.dp, top = 16.dp)
                .size(26.dp),
            onClick = {
                if (text.isNotEmpty()) {
                    coroutineScope.launch {
                        note.content = text
                        noteViewModel.saveNote(note)
                    }
                }
               paramNavController.navigate(Screen.Home.route)

            }
        ) {

            Icon(
                tint = if (isLayoutText) colorResource(id = R.color.blue_green) else colorResource(
                    id = R.color.darker_blue
                ),
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null
            )
        }

        Icon(
            tint = if (isLayoutText) colorResource(id = R.color.blue_green) else colorResource(id = R.color.darker_blue),
            imageVector = Icons.Default.EditNote,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(icon) {
                    start.linkTo(returnIcon.end)
                    top.linkTo(parent.top)
                }
                .padding(start = 4.dp, top = 16.dp)
                .size(26.dp)
        )



        TextButton(
            modifier = Modifier
                .constrainAs(doneBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(4.dp),
            onClick = {
                isLayoutText = !isLayoutText
            }
        ) {
            Text(
                text = if (!isLayoutText) "Done" else "Edit",
                fontSize = 18.sp,
                color = colorResource(id = R.color.darker_blue)
            )
        }


        if (!isLayoutText) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    note.content = text
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
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .padding(top = 56.dp),
                placeholder = {

                    note.content.ifBlank { "New note" }.let {
                        Text(
                            text = it
                        )
                    }
                }
            )
        } else {
                Text(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .constrainAs(contentTF) {
                            top.linkTo(icon.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }
                        .padding(8.dp),
                    text = note.content
                )
        }
    }
}