package com.itzik.mynotes.project.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.screens.note_screens.PinnedNoteListItem
import com.itzik.mynotes.project.screens.sections.CircleWithIcon
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun PinnedNotesScreen(
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    userViewModel: UserViewModel,
    user: User,
    noteViewModel: NoteViewModel
) {
    val pinnedNoteList by noteViewModel.publicPinnedNoteList.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.light_deep_purple),
                                Color.White
                            )
                        )
                    )
            ) {
                item {
                    CircleWithIcon(
                        circleColor = Color.White,
                        circleSize = 130.dp,
                        iconSize = 70.dp,
                        tint = colorResource(id = R.color.navy_blue),
                        imageVector = Icons.Default.PushPin,
                        borderColor = Color.Black,
                        borderThickness = 1.dp,
                        modifier = Modifier
                            .graphicsLayer(rotationZ = 20f)
                            .padding(40.dp)
                    )
                }
                items(pinnedNoteList) {noteItem->
                    PinnedNoteListItem(
                        modifier =modifier,
                        note =noteItem,
                        noteViewModel =noteViewModel,
                        coroutineScope =coroutineScope
                    )
                }
            }
        }
    }
}


