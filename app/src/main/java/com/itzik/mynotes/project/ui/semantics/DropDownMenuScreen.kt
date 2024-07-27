package com.itzik.mynotes.project.ui.semantics

import android.annotation.SuppressLint
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SortDropDownMenu(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteViewModel: NoteViewModel,
    isExpandedParam: Boolean,
) {
    var isExpanded by remember { mutableStateOf(isExpandedParam) }
    val sortOptions = listOf("Sort by date modified", "Sort alphabetically")

        DropdownMenu(
            modifier = modifier,
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            sortOptions.forEach {sortOption->
                DropdownMenuItem(
                    text = {
                        sortOption
                    }, onClick = {

                })
        }
    }
}