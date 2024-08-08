package com.itzik.mynotes.project.ui.semantics

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SortDropDownMenu(
    updatedSortedList: (String) -> Unit,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    noteViewModel: NoteViewModel,
    isExpanded: Boolean,
    onDismissRequest: () -> Unit
) {

    val sortOptions: List<String> = listOf("Sort by date modified", "Sort alphabetically")

    DropdownMenu(
        modifier = modifier,
        expanded = isExpanded,
        onDismissRequest = onDismissRequest
    ) {
        sortOptions.forEach {
            DropdownMenuItem(
                onClick = {
                    coroutineScope.launch {
                        noteViewModel.getSortedNotes(it)
                        updatedSortedList(it)
                    }
                    onDismissRequest()
                }
            ) {
                Text(text = it)
            }
        }
    }
}