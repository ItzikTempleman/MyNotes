package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Main
import com.itzik.mynotes.project.model.WeatherItem
import com.itzik.mynotes.project.model.WeatherResponse
import com.itzik.mynotes.project.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repo: AppRepository
) : ViewModel() {

    private val privateWeatherInstance = MutableStateFlow(
        WeatherResponse(
            main = Main(temp = 0.0),
            weather = listOf(WeatherItem(icon = ""))
        )
    )
    val publicWeatherInstance: MutableStateFlow<WeatherResponse> get() = privateWeatherInstance

    suspend fun getWeather(cityName: String) {
        viewModelScope.launch {
            val weather = repo.getWeather(cityName)
            privateWeatherInstance.value = weather
        }
    }


}