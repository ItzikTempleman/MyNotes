package com.itzik.mynotes.project.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WallpaperResponse(
    val hits: List<Hits>
) : Parcelable

@Parcelize
data class Hits(
    val previewURL: String,
    val webFormatURL: String,
    val largeImageURL: String,
) : Parcelable