package com.itzik.mynotes.project.ui.screen_sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.viewmodels.NoteViewModel

@Composable
fun NoteListItemForGrid(
    modifier: Modifier,
    note: Note,
    noteViewModel: NoteViewModel,
    isSelected: Boolean,
) {


















    
}




























//@Composable
//fun NoteListItemForGrid(
//    modifier: Modifier,
//    note: Note,
//    noteViewModel: NoteViewModel,
//    isSelected: Boolean,
//) {
//    var isOptionVisible by remember {
//        mutableStateOf(isSelected)
//    }
//
//    LaunchedEffect(isSelected) {
//        isOptionVisible = isSelected
//    }
//
//    val pinStateMap by noteViewModel.publicPinStateMap.collectAsState()
//    val starStateMap by noteViewModel.publicStarStateMap.collectAsState()
//
//    val isPinned = pinStateMap[note.noteId] ?: false
//    val isStarred = starStateMap[note.noteId] ?: false
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(180.dp)
//            .padding(8.dp) // Apply padding first
//            .drawBehind {
//                drawFoldingCornerEffect(16.dp, 12.dp) // Call the updated effect with adjusted size
//            },
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        ConstraintLayout(
//            modifier = modifier
//                .fillMaxSize()
//        ) {
//            val (content, pinnedNoteIcon, likedNoteIcon) = createRefs()
//
//            Text(
//                modifier = Modifier
//                    .constrainAs(content) {
//                        start.linkTo(parent.start)
//                        top.linkTo(parent.top)
//                    }
//                    .padding(8.dp),
//                text = note.content,
//                fontSize = 20.sp
//            )
//
//            if (isStarred) {
//                Icon(
//                    modifier = Modifier
//                        .constrainAs(likedNoteIcon) {
//                            end.linkTo(parent.end)
//                            bottom.linkTo(parent.bottom)
//                        }
//                        .size(30.dp)
//                        .padding(end = 8.dp),
//                    imageVector = Icons.Default.Star,
//                    contentDescription = null,
//                    tint = colorResource(id = R.color.muted_yellow)
//                )
//            }
//
//            if (isPinned) {
//                Icon(
//                    imageVector = Icons.Default.PushPin,
//                    modifier = Modifier
//                        .constrainAs(pinnedNoteIcon) {
//                            end.linkTo(parent.end)
//                            bottom.linkTo(parent.bottom)
//                        }
//                        .padding(end = 40.dp)
//                        .size(24.dp)
//                        .rotate(45f),
//                    tint = colorResource(id = R.color.light_purple),
//                    contentDescription = null
//                )
//            }
//        }
//    }
//}
//
//fun DrawScope.drawFoldingCornerEffect(cornerSize: Dp, cornerRadius: Dp) {
//    val foldSizePx = cornerSize.toPx()
//    val cornerRadiusPx = cornerRadius.toPx()
//    val cardWidth = size.width
//    val cardHeight = size.height
//
//    // Define the path for the folded corner with a rounded inner corner
//    val foldPath = Path().apply {
//        moveTo(cardWidth - foldSizePx, 0f) // Start before the top-right corner
//        lineTo(cardWidth, 0f) // Top-right corner
//        lineTo(cardWidth, foldSizePx) // Down the side fold
//        arcTo(
//            rect = androidx.compose.ui.geometry.Rect(
//                left = cardWidth - foldSizePx,
//                top = 0f,
//                right = cardWidth,
//                bottom = foldSizePx
//            ),
//            startAngleDegrees = 0f,
//            sweepAngleDegrees = -90f,
//            forceMoveTo = false
//        )
//        close() // Close the path to create the fold
//    }
//
//    // Draw the folded corner overlay with a different color for visibility
//    drawPath(
//        path = foldPath,
//        color = Color.LightGray,
//        style = Fill
//    )
//}
