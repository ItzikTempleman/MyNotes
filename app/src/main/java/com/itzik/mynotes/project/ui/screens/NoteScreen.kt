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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
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
                selection = TextRange.Zero
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
        val (backBtn, fontEditRow, pin, star, colorPickerScreen, contentTF) = createRefs()

        GenericIconButton(
            modifier = Modifier
                .constrainAs(backBtn) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(start = 8.dp, top = 16.dp)
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

        Card(
            modifier = Modifier.padding(8.dp)
                .constrainAs(fontEditRow) {
                    top.linkTo(parent.top)
                    start.linkTo(backBtn.end)
                }
                .width(180.dp).height(45.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(12.dp),

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    modifier = Modifier.padding(4.dp)
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
                    modifier = Modifier.padding(4.dp)
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

                Text(
                    modifier = Modifier.clickable {
                        if (textFieldValue.selection.start != textFieldValue.selection.end) {

                            val selectedTextRange = textFieldValue.selection
                            val selectedText = textFieldValue.text.substring(
                                textFieldValue.selection.start,
                                textFieldValue.selection.end
                            )

                            val annotatedString = buildAnnotatedString {
                                append(
                                    textFieldValue.text.substring(
                                        0,
                                        selectedTextRange.start
                                    )
                                )
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(selectedText)
                                }
                                append(textFieldValue.text.substring(selectedTextRange.end))
                            }

                            textFieldValue = textFieldValue.copy(
                                annotatedString = annotatedString,
                                selection = TextRange.Zero
                            )

                            coroutineScope.launch {
                                note.content = textFieldValue.text
                                noteViewModel.updateSelectedNoteContent(
                                    newChar = note.content,
                                    userId = note.userId,
                                    noteId = note.noteId,
                                    isPinned = note.isPinned,
                                    isStarred = note.isStarred,
                                    fontSize = note.fontSize,
                                    fontColor = note.fontColor
                                )
                            }
                        }
                    }.padding(4.dp),
                    text = "B",
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier.clickable {

                    }.padding(4.dp),
                    text = "B",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )

                VerticalDivider(modifier = Modifier.padding(12.dp))

                IconButton(
                    modifier = Modifier.padding(4.dp),
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
            }
        }


        if (note.isPinned) {
            Icon(
                modifier = Modifier.padding(8.dp)
                    .constrainAs(pin) {
                        top.linkTo(parent.top)
                        end.linkTo(star.start)
                    }
                    .rotate(45f),
                imageVector = Icons.Default.PushPin,
                contentDescription = null,
                tint = colorResource(id = R.color.light_purple)
            )
        }

        if (note.isStarred) {
            Icon(
                modifier = Modifier.padding(8.dp)
                    .constrainAs(star) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = colorResource(id = R.color.muted_yellow),
            )
        }

        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                coroutineScope.launch {
                    noteViewModel.updateSelectedNoteContent(
                        newValue.annotatedString.text,
                        noteId = noteId,
                        isPinned = note.isPinned,
                        isStarred = note.isStarred,
                        fontSize = fontSize,
                        fontColor = note.fontColor,
                        userId = note.userId,

                        )
                }
                if (!newValue.selection.collapsed) {
                   //TODO
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
                    top.linkTo(backBtn.bottom)
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
                            userId = note.userId,

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