package com.itzik.mynotes.project.ui.semantics

import android.annotation.SuppressLint
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenu
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SortDropDownMenu(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    noteViewModel: NoteViewModel,
    isExpandedParam: Boolean,
    onDismissRequest: () -> Unit
) {

    val sortOptions: List<String> = listOf("Sort by date modified", "Sort alphabetically")

    DropdownMenu(
        modifier = modifier,
        expanded = isExpandedParam,
        onDismissRequest = onDismissRequest
    ) {
        sortOptions.forEach {option->
            DropdownMenuItem(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = option)
            }
        }
    }
}