package com.itzik.mynotes.project.main

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.itzik.mynotes.project.ui.navigation.RootNavHost
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import com.itzik.mynotes.ui.theme.MyNotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired: Boolean = false

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 100
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100).build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest, it, Looper.getMainLooper()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            var currentLocation by remember {
                mutableStateOf(LatLng(0.0, 0.0))
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    for (location in result.locations) {
                        currentLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            }

            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            val navController: NavHostController = rememberNavController()




            userViewModel = viewModel()
            noteViewModel = viewModel()
            locationViewModel = viewModel()

            MyNotesTheme {

                RootNavHost(
                    locationViewModel = locationViewModel,
                    noteViewModel = noteViewModel,
                    context = this,
                    locationRequired = locationRequired,
                    startLocationUpdates = { startLocationUpdates() },
                    currentLocation = currentLocation,
                    userViewModel = userViewModel,
                    coroutineScope = coroutineScope,
                    navController = navController,
                    updateIsLocationRequired = {
                        locationRequired = it
                    }
                )
            }
        }
    }
}
