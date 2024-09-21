package com.itzik.mynotes.project.requests

import com.itzik.mynotes.project.model.WallpaperResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperService {

    @GET("?image_type=photo")
    suspend fun getWallpaperListBySearchQuery(
        @Query("key") apiKey: String,
        @Query("q") searchQuery: String
    ): Response<WallpaperResponse>

}