package com.itzik.mynotes.project.ui.composable_elements


import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.itzik.mynotes.R

@Composable
fun GenericIconButton(
    modifier:Modifier=Modifier,
    onClick:()->Unit,
    imageVector:ImageVector,
    iconSize:Modifier=Modifier.size(32.dp),
    colorNumber: Int
){
    IconButton(
        modifier =modifier,
        onClick = {
            onClick()
        }
    ) {
        Icon(modifier =iconSize , imageVector = imageVector, contentDescription =null , tint = when(colorNumber) {
            0 -> colorResource(id = R.color.blue_green)
            1 -> colorResource(id = R.color.light_yellow)
            2 -> colorResource(id = R.color.light_deep_purple)
            3 -> Color.Gray
            else -> {
                Color.DarkGray
            }
        })
    }
}