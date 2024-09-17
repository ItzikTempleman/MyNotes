package com.itzik.mynotes.project.requests

import com.itzik.mynotes.project.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApiService {
    @GET("{cityName}/EN")
    suspend fun getWeather(
        @Path("cityName")
        cityName: String
    ): WeatherResponse

}