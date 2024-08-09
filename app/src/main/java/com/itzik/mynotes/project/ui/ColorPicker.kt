package com.itzik.mynotes.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerDialog(
    modifier: Modifier,
        onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow,
        Color.Cyan, Color.Magenta, Color.Gray, Color.Black
    )

    Surface(
        modifier = modifier
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                colors.forEach {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(it, CircleShape)
                            .clickable {
                                onColorSelected(it)
                                onDismiss()
                            }
                    )
                }
            }
        }
    }
}