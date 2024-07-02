package com.itzik.mynotes.project.screens.sections

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@Composable
fun CircleWithIcon(
    circleColor: Color,
    circleSize: Dp,
    iconSize: Dp,
    imageVector:ImageVector,
    modifier: Modifier,
    tint:Color,
    borderColor:Color,
    borderThickness: Dp
) {
    Box(
        modifier = modifier.size(circleSize),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.size(circleSize)) {
            val radius = size.minDimension / 2
            val strokeWidthPx = borderThickness.toPx()
            drawCircle(
                color = circleColor,
                radius = radius - strokeWidthPx / 2,
                center = Offset(radius, radius)
            )
            drawCircle(
                color = borderColor,
                radius = radius - strokeWidthPx / 2,
                center = Offset(radius, radius),
                style = Stroke(width = strokeWidthPx)
            )
        }


        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = tint,
            modifier = modifier.size(iconSize)
        )
    }
}