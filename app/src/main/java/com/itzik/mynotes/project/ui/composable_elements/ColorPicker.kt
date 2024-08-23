package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.itzik.mynotes.R

@Composable
fun ColorPickerDialog(
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val colorList = listOf(
        colorResource(id = R.color.deep_purple),
        colorResource(id = R.color.deeper_purple),
        colorResource(id = R.color.mid_purple),
        colorResource(id = R.color.light_purple),
        colorResource(id = R.color.very_light_purple),
        colorResource(id = R.color.purple_blue),
        colorResource(id = R.color.steel_blue),
        colorResource(id = R.color.light_steel_blue),
        colorResource(id = R.color.navy_blue),
        colorResource(id = R.color.darker_blue),
        colorResource(id = R.color.deep_blue),
        colorResource(id = R.color.lighter_blue),
        colorResource(id = R.color.blue_green),
        colorResource(id = R.color.semi_transparent_blue_green),
        colorResource(id = R.color.very_light_green),
        colorResource(id = R.color.light_green),
        colorResource(id = R.color.bb_green),
        colorResource(id = R.color.dark_green),
        colorResource(id = R.color.muted_yellow),
        colorResource(id = R.color.bb_orange),
        colorResource(id = R.color.light_pink),
        colorResource(id = R.color.pink),
        colorResource(id = R.color.light_red),
        colorResource(id = R.color.red),
        colorResource(id = R.color.black),
        colorResource(id = R.color.dark_gray),
        colorResource(id = R.color.gray),
        colorResource(id = R.color.light_gray),

    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(colorList) { color ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .size(50.dp)
                        .background(color)
                        .clickable {
                            onColorSelected(color)
                            onDismiss()
                        }
                )
            }
        }

    }
}