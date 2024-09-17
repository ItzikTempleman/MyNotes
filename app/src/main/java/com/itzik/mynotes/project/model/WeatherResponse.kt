package com.itzik.mynotes.project.model

import android.os.Parcelable
import com.itzik.mynotes.project.utils.WEATHER_ICON_URL
import com.itzik.mynotes.project.utils.WEATHER_ICON_URL_ENDING
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherItem>
) : Parcelable

@Parcelize
data class Main(
    val temp: Double,
) : Parcelable

@Parcelize
data class WeatherItem(
    val icon:String
): Parcelable {
    fun getImage() = WEATHER_ICON_URL + icon + WEATHER_ICON_URL_ENDING
}


