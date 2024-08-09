package com.itzik.mynotes.project.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.itzik.mynotes.project.ui.semantics.GenericIconButton
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NoteScreen(
    noteId: Int?,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    paramNavController: NavHostController
) {
    val note by noteViewModel.publicNote.collectAsState()
    var text by remember { mutableStateOf(note.content) }
    var isColorPickerOpen by remember { mutableStateOf(false) }
    var fontSize by remember { mutableIntStateOf(note.fontSize) }
    var selectedColor by remember { mutableIntStateOf(note.fontColor) }


        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val (topRow, contentTF, colorPickerScreen) = createRefs()

            Card(
                modifier = Modifier
                    .constrainAs(topRow) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (backBtn, fontSmaller, fontLarger, fontSizeIndicator, changeFontColorDialog, pin, star) = createRefs()


                    GenericIconButton(
                        modifier = Modifier
                            .constrainAs(backBtn) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
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
                        },
                        imageVector = Icons.Default.ArrowBackIosNew,
                        colorNumber = 0
                    )


                    Text(
                        modifier = Modifier
                            .constrainAs(fontSmaller) {
                                top.linkTo(parent.top)
                                start.linkTo(backBtn.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(start = 40.dp)
                            .clickable {
                                if (fontSize > 10) {
                                    coroutineScope.launch {
                                        fontSize -= 2
                                        noteViewModel.updateSelectedNoteContent(
                                            text,
                                            noteId,
                                            note.isPinned,
                                            note.isStarred,
                                            fontSize,
                                            note.fontColor
                                        )
                                    }
                                }
                            },
                        text = "A",
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(fontLarger) {
                                top.linkTo(parent.top)
                                start.linkTo(fontSmaller.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(start = 8.dp)
                            .clickable {
                                if (fontSize < 40) {
                                    coroutineScope.launch {
                                        fontSize += 2
                                        noteViewModel.updateSelectedNoteContent(
                                            text,
                                            noteId,
                                            note.isPinned,
                                            note.isStarred,
                                            fontSize,
                                            note.fontColor
                                        )
                                    }
                                }
                            },
                        fontWeight = FontWeight.Bold,
                        text = "A",
                        fontSize = 26.sp,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(fontSizeIndicator) {
                                top.linkTo(parent.top)
                                start.linkTo(fontLarger.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(start = 8.dp),
                        text = "$fontSize sp",
                        fontSize = 16.sp

                    )
                    IconButton(
                        modifier = Modifier
                            .constrainAs(changeFontColorDialog) {
                                top.linkTo(parent.top)
                                start.linkTo(fontSizeIndicator.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(horizontal = 30.dp),
                        onClick = {
                            isColorPickerOpen = !isColorPickerOpen
                        }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.color_palette),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }




                    if (note.isPinned) {
                        Icon(
                            modifier = Modifier
                                .constrainAs(pin) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(horizontal = 40.dp)
                                .rotate(45f),
                            imageVector = Icons.Default.PushPin,
                            contentDescription = null,
                            tint = colorResource(id = R.color.light_deep_purple)
                        )
                    }
                    if (note.isStarred) {
                        Icon(
                            modifier = Modifier
                                .padding(4.dp)
                                .constrainAs(star) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                },
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = colorResource(id = R.color.light_yellow),

                            )
                    }
                }

            }
            Log.d("NoteScreen", "Selected color: $selectedColor")
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    coroutineScope.launch {
                        noteViewModel.updateSelectedNoteContent(
                            it,
                            noteId,
                            note.isPinned,
                            note.isStarred,
                            note.fontSize,
                            note.fontColor
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
                        top.linkTo(topRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                textStyle = TextStyle.Default.copy(
                    fontSize = fontSize.sp,
                    color = Color(selectedColor).copy(alpha = 1.0f),
                    fontWeight = FontWeight.Bold
                ),
                placeholder = {
                    Text(
                        fontSize = fontSize.sp,
                        color =     Color(selectedColor).copy(alpha = 1.0f),
                        fontWeight = FontWeight.Bold,
                        text = note.content.ifEmpty { stringResource(id = R.string.new_note) }
                    )
                }
            )


            if (isColorPickerOpen) {
                ColorPickerDialog(
                    modifier = Modifier
                        .constrainAs(colorPickerScreen) {
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp, vertical = 60.dp),
                    onColorSelected = {color ->
                        coroutineScope.launch {
                            selectedColor = color.toArgb()
                            noteViewModel.updateSelectedNoteContent(
                                text,
                                noteId,
                                note.isPinned,
                                note.isStarred,
                                fontSize,
                                selectedColor
                            )
                        }
                        isColorPickerOpen = false
                    },
                    onDismiss = {
                        isColorPickerOpen = false
                    }
                )
            }
        }
    }
