package com.itzik.mynotes.project.screens


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.itzik.mynotes.R
import com.itzik.mynotes.project.screens.navigation.Screen
import com.itzik.mynotes.project.screens.note_screens.NoteListItem
import com.itzik.mynotes.project.utils.convertLatLangToLocation
import com.itzik.mynotes.project.viewmodels.LocationViewModel
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.CoroutineScope


private val permissions = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
)

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun HomeScreen(
    locationViewModel: LocationViewModel,
    context: Context,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    noteViewModel: NoteViewModel,
    currentLocation: LatLng,
    locationRequired: Boolean,
    startLocationUpdates: () -> Unit,
    updateIsLocationRequired: (Boolean) -> Unit,
) {

    var isLoadingLocation by remember {
        mutableStateOf(false)
    }

    var mutableLocationRequired by remember {
        mutableStateOf(locationRequired)
    }
    var locationName by remember {
        mutableStateOf("")
    }

    val noteList by noteViewModel.exposedNoteList.collectAsState()

    val launchMultiplePermissions =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
            val areGranted = it.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                mutableLocationRequired = true
                startLocationUpdates()
            }
            Toast.makeText(
                context,
                if (areGranted) "Permission Granted" else "Permission Declined",
                Toast.LENGTH_SHORT
            ).show()
            updateIsLocationRequired(mutableLocationRequired)
        }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val (icon,locationButton, noteLazyColumn, newNoteBtn, progressBar) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.blue_green),
                            Color.White
                        )
                    )
                )
        ) {

            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = colorResource(id = R.color.darker_blue),
                        modifier = Modifier.size(30.dp),
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Home",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.darker_blue),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .constrainAs(noteLazyColumn) {
                    top.linkTo(icon.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth().background(Color.White)
        ) {
            items(noteList) { noteItem ->
                NoteListItem(
                    isTrashed = false,
                    noteViewModel = noteViewModel,
                    coroutineScope = coroutineScope,
                    note = noteItem,
                    modifier = Modifier.clickable {
                        val noteJson = Uri.encode(Gson().toJson(noteItem))
                        navController.navigate("${Screen.NoteScreen.route}/$noteJson")
                    },
                    updatedList = { updatedNotes ->
                        noteViewModel.setNoteList(updatedNotes)
                    }
                )
            }
        }

        FloatingActionButton(
            shape = CircleShape,
            containerColor = colorResource(id = R.color.darker_blue),
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(newNoteBtn) {
                    end.linkTo(parent.end)
                    bottom.linkTo(locationButton.top)
                }, onClick = {
                navController.navigate(Screen.NoteScreen.route)
            }
        ) {
            Icon(
                Icons.Filled.Add,
                null, tint = Color.White
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            containerColor = Color.White,
            modifier = Modifier
                .constrainAs(locationButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(8.dp),
            onClick = {
                isLoadingLocation = true
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context, it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    startLocationUpdates()
                    locationName = convertLatLangToLocation(currentLocation, context)
                    locationViewModel.setLocationName(convertLatLangToLocation(currentLocation, context))
                    if (locationName.isNotBlank())
                        isLoadingLocation = false
                } else {
                    launchMultiplePermissions.launch(permissions)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = colorResource(
                    id = R.color.darker_blue
                )
            )
        }


        if (isLoadingLocation) {
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}
