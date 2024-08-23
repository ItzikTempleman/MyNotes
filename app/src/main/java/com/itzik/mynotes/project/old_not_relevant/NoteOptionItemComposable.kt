package com.itzik.mynotes.project.old_not_relevant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun NoteOptionItemComposable(
    modifier: Modifier,
    note: Note,
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
                noteOptionsRows.onClick(
                    note,
                    noteViewModel,
                    coroutineScope,
                    navController
                )
            }, colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = if (noteOptionsRows.title == "Pin note") Modifier
                    .padding(4.dp)
                    .size(30.dp)
                    .rotate(45f) else Modifier
                    .padding(4.dp)
                    .size(30.dp),
                imageVector = noteOptionsRows.icon,
                contentDescription = null,
                tint = when (noteOptionsRows.title) {
                    "Star note" -> colorResource(id = R.color.muted_yellow)
                    "Pin note" -> colorResource(id = R.color.muted_yellow)
                    else -> colorResource(id = R.color.blue_green)
                }
            )
            Text(
                modifier = Modifier.padding(2.dp),
                text = noteOptionsRows.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}