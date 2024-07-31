package com.itzik.mynotes.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun NoteOptionSectionItemScreen(
    modifier:Modifier,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteOptionsRows: NoteOptionsRows
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .width(125.dp)
            .aspectRatio(1f)
            .clickable {
                noteOptionsRows.onClick(noteViewModel, coroutineScope, navController)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier =
                if(noteOptionsRows.title=="Pin note") Modifier.padding(6.dp).size(36.dp).rotate(45f)  else  Modifier
                    .padding(6.dp)
                    .size(36.dp),
                imageVector = noteOptionsRows.icon, contentDescription = null, tint = if(noteOptionsRows.title=="Star note") colorResource(
                    id = R.color.light_yellow) else if (noteOptionsRows.title=="Pin note") colorResource(
                    id = R.color.light_deep_purple
                )
                    else colorResource(id = R.color.deep_blue)
            )

            Text(
                modifier = Modifier
                    .padding(6.dp),
                text = noteOptionsRows.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun NoteOptionsLayout(
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    modifier: Modifier,
    note: Note
) {
    val optionItems = listOf(
        NoteOptionsRows.PinNote.apply { this.note = note },
        NoteOptionsRows.DeleterNote.apply { this.note = note },
        NoteOptionsRows.StarNote.apply { this.note = note }
    )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color.White)

        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                items(optionItems) { listItem ->
                    NoteOptionSectionItemScreen(
                        modifier=modifier,
                        noteViewModel = noteViewModel,
                        coroutineScope = coroutineScope,
                        navController = navController,
                        noteOptionsRows = listItem
                    )
                }
            }

    }
}