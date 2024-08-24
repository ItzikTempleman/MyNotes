package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.itzik.mynotes.project.utils.provideColorList

@Composable
fun ColorPickerDialog(
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val colorList = provideColorList()
    val outerPadding = 16.dp
    val innerPadding = 16.dp
    val borderWidth = 0.75.dp
    val roundedCornerSize = 12.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val totalPadding = outerPadding * 2 + innerPadding * 2
    val availableWidth = screenWidth - totalPadding
    val boxSize = remember { (availableWidth / (availableWidth / 50.dp)).coerceAtMost(50.dp) }
    val numberOfColumns = (availableWidth / boxSize).toInt()
    val numberOfRows = (colorList.size + numberOfColumns - 1) / numberOfColumns

    Box(
        modifier = modifier
            .padding(outerPadding)
            .clip(RoundedCornerShape(roundedCornerSize))
            .background(Color.White, shape = RoundedCornerShape(roundedCornerSize))
            .border(BorderStroke(borderWidth, Color.Black), shape = RoundedCornerShape(roundedCornerSize))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (rowIndex in 0 until numberOfRows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    for (columnIndex in 0 until numberOfColumns) {
                        val itemIndex = rowIndex * numberOfColumns + columnIndex
                        if (itemIndex < colorList.size) {
                            val color = colorList[itemIndex]
                            Box(
                                modifier = Modifier
                                    .size(boxSize)
                                    .background(color)
                                    .clickable {
                                        onColorSelected(color)
                                        onDismiss()
                                    },
                            )
                        } else {
                            Spacer(modifier = Modifier.size(boxSize))
                        }
                    }
                }
            }
        }
    }
}