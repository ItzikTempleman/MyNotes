package com.itzik.mynotes.project.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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


    var isBoldDialogOpen by remember { mutableStateOf(false) }
    //var text by remember { mutableStateOf(note.content) }
    var isColorPickerOpen by remember { mutableStateOf(false) }
    var fontSize by remember { mutableIntStateOf(note.fontSize) }
    var selectedColor by remember { mutableIntStateOf(note.fontColor) }
    val focusManager = LocalFocusManager.current

    BackHandler {
        if (textFieldValue.text.isNotEmpty()) {
            coroutineScope.launch {
                note.content = textFieldValue.text
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
        val (backBtn, fontSmaller, fontLarger, fontSizeIndicator, changeFontColorDialog, pin, star, colorPickerScreen, contentTF) = createRefs()
        GenericIconButton(
            modifier = Modifier
                .constrainAs(backBtn) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
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
                .constrainAs(fontSmaller) {
                    top.linkTo(backBtn.top)
                    start.linkTo(backBtn.end)
                    bottom.linkTo(backBtn.bottom)
                }
                .padding(start = 40.dp)
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
            fontSize = 20.sp,
            color = Color.Black
        )

        Text(
            modifier = Modifier
                .constrainAs(fontLarger) {
                    top.linkTo(backBtn.top)
                    start.linkTo(fontSmaller.end)
                    bottom.linkTo(backBtn.bottom)
                }
                .padding(start = 8.dp)
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
                                userId = note.userId,

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
                    top.linkTo(backBtn.top)
                    start.linkTo(fontLarger.end)
                    bottom.linkTo(backBtn.bottom)
                }
                .padding(start = 8.dp),
            text = "$fontSize sp",
            fontSize = 16.sp, color = Color.Black
        )
        IconButton(
            modifier = Modifier
                .constrainAs(changeFontColorDialog) {
                    top.linkTo(backBtn.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(backBtn.bottom)
                }
                .padding(4.dp),
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

        if (note.isPinned) {
            Icon(
                modifier = Modifier
                    .constrainAs(pin) {
                        top.linkTo(changeFontColorDialog.top)
                        bottom.linkTo(changeFontColorDialog.bottom)
                        end.linkTo(changeFontColorDialog.start)
                    }
                    .padding(horizontal = 44.dp)
                    .rotate(45f),
                imageVector = Icons.Default.PushPin,
                contentDescription = null,
                tint = colorResource(id = R.color.light_purple)
            )
        }
        if (note.isStarred) {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .constrainAs(star) {
                        top.linkTo(changeFontColorDialog.top)
                        bottom.linkTo(changeFontColorDialog.bottom)
                        end.linkTo(changeFontColorDialog.start)
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
                        textFieldValue.text,
                        noteId = noteId,
                        isPinned = note.isPinned,
                        isStarred = note.isStarred,
                        fontSize = fontSize,
                        fontColor = note.fontColor,
                        userId = note.userId,

                        )
                }
                if (!newValue.selection.collapsed) {
                    isBoldDialogOpen = true
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
                fontWeight = FontWeight.Bold
            ),
            placeholder = {
                Text(
                    fontSize = fontSize.sp,
                    color = Color(selectedColor),
                    fontWeight = FontWeight.Bold,
                    text = note.content.ifEmpty { stringResource(id = R.string.new_note) }
                )
            }
        )


        if (isBoldDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    isBoldDialogOpen = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val selectedText = textFieldValue.text.substring(
                                textFieldValue.selection.start,
                                textFieldValue.selection.end
                            )
                            val updatedText =
                                textFieldValue.text.substring(0, textFieldValue.selection.start) +
                                        selectedText +
                                        textFieldValue.text.substring(textFieldValue.selection.end)

                            textFieldValue =
                                textFieldValue.copy(text = updatedText, selection = TextRange.Zero)

                            coroutineScope.launch {
                                    note.content = textFieldValue.text
                                    noteViewModel.saveNote(note)
                                }
                            isBoldDialogOpen = false
                        }
                    ) {
                        Text(text = "BOLD", fontWeight = FontWeight.Bold)
                    }
                }, dismissButton = {}

            )
        }

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