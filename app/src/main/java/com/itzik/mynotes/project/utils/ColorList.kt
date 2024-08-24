package com.itzik.mynotes.project.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.itzik.mynotes.R

@Composable
fun provideColorList(): List<Color> {
   return listOf(
        colorResource(id = R.color.deeper_purple),
        colorResource(id = R.color.deep_purple),
        colorResource(id = R.color.dark_purple),
        colorResource(id = R.color.purple),
        colorResource(id = R.color.light_purple),
        colorResource(id = R.color.navy_blue),
        colorResource(id = R.color.darker_blue),
        colorResource(id = R.color.deep_blue),
        colorResource(id = R.color.purple_blue),
        colorResource(id = R.color.dark_steel_blue),
        colorResource(id = R.color.blue_green),
        colorResource(id = R.color.light_steel_blue),
        colorResource(id = R.color.lighter_blue),
        colorResource(id = R.color.sky_blue),
        colorResource(id = R.color.aqua_blue),
        colorResource(id = R.color.semi_transparent_blue_green),
        colorResource(id = R.color.very_light_green),
        colorResource(id = R.color.light_green),
        colorResource(id = R.color.spring_green),
        colorResource(id = R.color.pale_green),
        colorResource(id = R.color.lime_green),
        colorResource(id = R.color.green),
        colorResource(id = R.color.dark_green),
        colorResource(id = R.color.muted_yellow),
        colorResource(id = R.color.yellow_green),
        colorResource(id = R.color.bright_yellow),
        colorResource(id = R.color.vibrant_orange),
        colorResource(id = R.color.burnt_orange),
        colorResource(id = R.color.charcoal_gray),
        colorResource(id = R.color.dark_gray),
        colorResource(id = R.color.gray),
        colorResource(id = R.color.light_gray),
        colorResource(id = R.color.medium_gray),
        colorResource(id = R.color.cool_gray),
        colorResource(id = R.color.slate_gray),
        colorResource(id = R.color.smoky_gray),
        colorResource(id = R.color.light_pink),
        colorResource(id = R.color.rose_pink),
        colorResource(id = R.color.peach_pink),
        colorResource(id = R.color.coral_pink),
        colorResource(id = R.color.pink),
        colorResource(id = R.color.black),
        colorResource(id = R.color.light_red),
        colorResource(id = R.color.red),
        colorResource(id = R.color.crimson),
        colorResource(id = R.color.fire_red),
        colorResource(id = R.color.deep_red),
        colorResource(id = R.color.cinnamon),
        colorResource(id = R.color.light_brown)
    )
}