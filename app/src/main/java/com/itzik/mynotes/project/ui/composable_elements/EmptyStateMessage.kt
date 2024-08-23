package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun EmptyStateMessage(
    modifier:Modifier,
    screenDescription:String?=""
) {
    Text(
        modifier = modifier, fontSize = 40.sp, color = Color.Gray, text = "No $screenDescription notes"
    )
}