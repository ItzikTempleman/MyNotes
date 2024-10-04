package com.itzik.mynotes.project.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.ui.composable_elements.BoldedTextSelectionButtons
import com.itzik.mynotes.project.ui.composable_elements.ColorPickerDialog
import com.itzik.mynotes.project.ui.composable_elements.GenericIconButton
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@Composable
fun NoteScreen(
    noteId: Int?,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    bottomBarNavController: NavHostController,
) {
    val note by noteViewModel.publicNote.collectAsState()

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = note.content,
            )
        )
    }

    var isColorPickerOpen by remember { mutableStateOf(false) }
    var fontSize by remember { mutableIntStateOf(note.fontSize) }
    var selectedColor by remember { mutableIntStateOf(note.fontColor) }
    val focusManager = LocalFocusManager.current

    BackHandler {
        if (textFieldValue.text.isNotEmpty()) {
            coroutineScope.launch {
                note.content = textFieldValue.annotatedString.text
                noteViewModel.saveNote(note)
            }
        }
        bottomBarNavController.popBackStack()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (topRow, colorPickerScreen, contentTF) = createRefs()

        Card(
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(topRow) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth()
                .height(45.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(12.dp),

            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GenericIconButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp),
                    onClick = {
                        if (textFieldValue.text.isNotEmpty()) {
                            coroutineScope.launch {
                                note.content = textFieldValue.text
                                noteViewModel.saveNote(note)
                            }
                        }
                        bottomBarNavController.popBackStack()
                    },
                    imageVector = Icons.Default.ArrowBackIosNew,
                    colorNumber = 4
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (fontSize > 10) {
                                coroutineScope.launch {
                                    fontSize -= 2
                                    noteViewModel.updateSelectedNoteContent(
                                        textFieldValue.text,
                                        noteId = noteId,
                                        isStarred = note.isPinned,
                                        isPinned = note.isStarred,
                                        fontSize = fontSize,
                                        fontColor = note.fontColor,
                                        userId = note.userId,
                                    )
                                }
                            }
                        },
                    text = "A",
                    fontSize = 20.sp
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (fontSize < 40) {
                                coroutineScope.launch {
                                    fontSize += 2
                                    noteViewModel.updateSelectedNoteContent(
                                        textFieldValue.text,
                                        noteId = noteId,
                                        isPinned = note.isPinned,
                                        isStarred = note.isStarred,
                                        fontSize = fontSize,
                                        fontColor = note.fontColor,
                                        userId = note.userId
                                    )
                                }
                            }
                        },
                    text = "A",
                    fontSize = 28.sp
                )

                VerticalDivider(modifier = Modifier.padding(12.dp))

                BoldedTextSelectionButtons(
                    modifier = Modifier.padding(8.dp),
                    isBolded = false,
                    textFieldValue =textFieldValue,
                    onValueChange = {
                        textFieldValue=it
                    }
                )

                BoldedTextSelectionButtons(
                    modifier = Modifier.padding(8.dp),
                    isBolded = true,
                    textFieldValue =textFieldValue,
                    onValueChange = {
                        textFieldValue=it
                    }
                )


                VerticalDivider(modifier = Modifier.padding(12.dp))

                IconButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        isColorPickerOpen = !isColorPickerOpen
                        focusManager.clearFocus()
                    }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.color_palette),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

                VerticalDivider(modifier = Modifier.padding(12.dp))

                if (note.isPinned) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal =8.dp)
                            .rotate(45f),
                        imageVector = Icons.Outlined.PushPin,
                        contentDescription = null,
                    )
                }

                if (note.isStarred) {
                    Icon(
                        modifier = Modifier
                            .padding(8.dp),
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                    )
                }
            }
        }

        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                coroutineScope.launch {
                    noteViewModel.updateSelectedNoteContent(
                        newChar = textFieldValue.annotatedString.text,
                        noteId = noteId,
                        isPinned = note.isPinned,
                        isStarred = note.isStarred,
                        fontSize = fontSize,
                        fontColor = note.fontColor,
                        userId = note.userId)
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
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(0.dp))
                .constrainAs(contentTF) {
                    top.linkTo(topRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
            textStyle = TextStyle.Default.copy(
                fontSize = fontSize.sp,
                color = Color(selectedColor),
            ),
            placeholder = {
                Text(
                    fontSize = fontSize.sp,
                    color = Color(selectedColor),
                    text = note.content.ifEmpty { stringResource(id = R.string.new_note) }
                )
            }
        )

        if (isColorPickerOpen) {
            ColorPickerDialog(
                modifier = Modifier
                    .constrainAs(colorPickerScreen) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 60.dp),
                onColorSelected = { color ->
                    coroutineScope.launch {
                        selectedColor = color.toArgb()
                        noteViewModel.updateSelectedNoteContent(
                            textFieldValue.text,
                            noteId = noteId,
                            isStarred = note.isPinned,
                            isPinned = note.isStarred,
                            fontSize = fontSize,
                            fontColor = selectedColor,
                            userId = note.userId)
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