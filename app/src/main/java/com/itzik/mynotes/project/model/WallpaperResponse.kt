package com.itzik.mynotes.project.model


data class WallpaperResponse(
    val hits: List<Hits>
)

data class Hits(
    val id:Int,
    val tags:String,
    val previewURL: String,
    val webFormatURL:String,
    val largeImageURL:String,
)