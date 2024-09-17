package com.itzik.mynotes.project.utils


import android.content.Context
import android.location.Geocoder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import java.util.Locale
import kotlin.math.roundToInt


fun convertLatLangToLocation(currentLocation: LatLng, context: Context): String {
    val latLangPair = splitLatLangToDoubles(currentLocation)
    val geoCoder = Geocoder(context, Locale.getDefault())
    var locationName = ""
    var countryName = ""
    val addresses = geoCoder.getFromLocation(latLangPair.first, latLangPair.second, 0)
    if (!addresses.isNullOrEmpty()) {
        val address = addresses[0]
        locationName = address.locality+", "+address.adminArea
        countryName = address.countryName ?: "country name not available"
    }
    return "$locationName, $countryName"
}

fun splitLatLangToDoubles(currentLocation: LatLng): Pair<Double, Double> {
    val latitude = currentLocation.latitude
    val longitude = currentLocation.longitude
    return Pair(latitude, longitude)
}


@Composable
fun TemperatureText(
    temperature: String,
    modifier: Modifier
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append(temperature)
            withStyle(
                style = SpanStyle(
                    baselineShift = BaselineShift(0.5f),
                    fontSize = 12.sp,
                )
            ) {
                append("o")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 18.sp,
                )
            ) {
                append("C")
            }
        },
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        color = Color.Black
    )
}

fun convertFahrenheitToCelsius(temp: Double): String {
    val newTemp = ((temp - 32) * 5) / 9
    return newTemp.roundToInt().toString()
}
