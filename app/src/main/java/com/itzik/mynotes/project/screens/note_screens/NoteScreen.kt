package com.itzik.mynotes.project.screens.note_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel,
    modifier: Modifier,
    note: Note,
    coroutineScope: CoroutineScope,
    userViewModel: UserViewModel,
    user: User,
    paramNavController: NavHostController,
) {


    var text by remember {
        mutableStateOf("")
    }

    paramNavController.currentBackStackEntry?.savedStateHandle?.set(
        key = "note",
        value = note
    )
                ConstraintLayout(
                    modifier = modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.blue_green))
                ) {
                    val (returnIcon, icon, contentTF) = createRefs()


                    IconButton(
                        modifier = Modifier
                            .constrainAs(returnIcon) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                            .padding(start = 4.dp, top = 16.dp)
                            .size(26.dp),
                        onClick = {
                            coroutineScope.launch {
                                if (text.isNotBlank()) {
                                    noteViewModel.updateNote(text)
                                }
                            }
                            paramNavController.navigate(Screen.Home.route)
                        }) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }

                    Icon(
                        tint = Color.White,
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
                                top.linkTo(icon.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.fillToConstraints
                            },
                        placeholder = {
                            Text(
                                text = if(note.content.isBlank()) "New note" else note.content
                            )
                        }
                    )
                }
            }