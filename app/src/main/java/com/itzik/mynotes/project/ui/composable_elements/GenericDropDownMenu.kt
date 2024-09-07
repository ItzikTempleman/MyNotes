package com.itzik.mynotes.project.ui.composable_elements

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
fun GenericDropDownMenu(
    updatedList: (String) -> Unit,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    noteViewModel: NoteViewModel? = null,
    isExpanded: Boolean? = null,
    isGenderExpanded: Boolean? = null,
    onDismissRequest: () -> Unit,
    isSelectSort: Boolean
) {

    val sortOptions: List<String> = listOf("Sort by date modified", "Sort alphabetically")
    val genderList: List<String> = listOf("Male", "Female", "Other")

    if (isSelectSort && isExpanded != null) {
            DropdownMenu(
                modifier = modifier,
                expanded = isExpanded,
                onDismissRequest = onDismissRequest
            ) {
                sortOptions.forEach {
                    DropdownMenuItem(
                        onClick = {
                            coroutineScope.launch {
                                noteViewModel?.getSortedNotes(it)
                                updatedList(it)
                            }
                            onDismissRequest()
                        }
                    ) {
                        Text(text = it)
                    }
                }
            }

    } else if (!isSelectSort && isGenderExpanded != null) {
            DropdownMenu(
                modifier = modifier,
                expanded = isGenderExpanded,
                onDismissRequest = onDismissRequest
            ) {
                genderList.forEach {
                    DropdownMenuItem(
                        onClick = {
                            updatedList(it)
                            onDismissRequest()
                        }
                    ) {
                        Text(text = it)
                    }
                }
            }
        }
}


