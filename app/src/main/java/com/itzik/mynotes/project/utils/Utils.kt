package com.itzik.mynotes.project.utils


import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


fun convertLatLangToLocation(currentLocation: LatLng, context: Context): String {
    val latLangPair = splitLatLangToDoubles(currentLocation)
    val geoCoder = Geocoder(context, Locale.getDefault())
    var locationName = ""
    var countryName = ""
    val addresses = geoCoder.getFromLocation(latLangPair.first, latLangPair.second, 0)
    if (!addresses.isNullOrEmpty()) {
        val address = addresses[0]
        locationName = address.getAddressLine(0)
        countryName = address.countryName ?: "country name not available"
    }
    return "$locationName, $countryName"
}

fun splitLatLangToDoubles(currentLocation: LatLng): Pair<Double, Double> {
    val latitude = currentLocation.latitude
    val longitude = currentLocation.longitude
    return Pair(latitude, longitude)
}


