package com.rudraksha.rone.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rudraksha.rone.ui.components.RCard
import com.rudraksha.rone.viewmodel.FitnessData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    context: Context,
    modifier: Modifier = Modifier,
    fitnessData: FitnessData = FitnessData(),
    startTracking: () -> Unit = {},
    stopTracking: () -> Unit = {}
) {
    val cameraPositionState = rememberCameraPositionState()
    var running by remember { mutableStateOf(false) }
    var mapType by remember { mutableStateOf(MapType.SATELLITE) }
    var showMapTypeDialog by remember { mutableStateOf(false) }
    val mapProperties = rememberUpdatedState(
        MapProperties(
            isBuildingEnabled = true,
            isIndoorEnabled = true,
            isMyLocationEnabled = true,
            isTrafficEnabled = true,
//        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, com.google.android.material.R.raw.style),
            mapType = mapType,
            maxZoomPreference = 30f,
            minZoomPreference = 1f
        )
    )

    var clickedLocation by remember { mutableStateOf<LatLng?>(null) }
    var searchText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val dispatchersScope = CoroutineScope(Dispatchers.Default)

    val permissionsList = remember { mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ) }

    var showPermissionButton by remember { mutableStateOf(true) }
    var permanentlyDenied by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { results ->
            coroutineScope.launch {
                showPermissionButton = results.any { !it.value }
                permanentlyDenied = results.any {
                    !it.value && !shouldShowRequestPermissionRationale(context, it.key)
                }

                Toast.makeText(context,
                    if (showPermissionButton) "Some permissions denied" else "All permissions granted",
                    Toast.LENGTH_SHORT
                ).show()

                if (permanentlyDenied) {
                    delay(300)
                    Toast.makeText(context,
                        "Permissions are permanently denied. Enable them in settings.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        dispatchersScope.launch {
            launcher.launch(permissionsList.toTypedArray())
        }
    }

    // Update camera position to the user's starting location
    LaunchedEffect(fitnessData.path) {
        dispatchersScope.launch {
            if (fitnessData.path.isNotEmpty()) {
                val lastPosition = fitnessData.path.first()
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            LatLng(lastPosition.latitude, lastPosition.longitude),
                            16f
                        )
                    ),
                    durationMs = 1000
                )
            }
        }
    }

    // Focus on the user's location at the start and as they move
    LaunchedEffect(fitnessData.currentLocation) {
        dispatchersScope.launch {
            fitnessData.currentLocation?.let { location ->
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            LatLng(location.latitude, location.longitude),
                            16f
                        )
                    ),
                    durationMs = 1000
                )
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { CenterAlignedTopAppBar(
            title = { Text(text = "Location Tracker") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        ) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (showPermissionButton) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOff,
                            contentDescription = "Request permission",
                            modifier = Modifier.fillMaxSize()
                        )
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    launcher.launch(permissionsList.toTypedArray())
                                }
                            }
                        ) {
                            Text(
                                text = "Grant permissions"
                            )
                        }
                    }
                }
                else {
                    // Create a search bar
                    TextField(
                        value = searchText,
                        onValueChange = { value -> searchText = value },
                        placeholder = { Text(text = "Search Location") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search Icon"
                            )
                        },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )

                    RCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        GoogleMap(
                            modifier = Modifier.weight(1f),
                            cameraPositionState = cameraPositionState,
                            properties = mapProperties.value,
                            onMapClick = { latlong ->
                                coroutineScope.launch {
//                                    Log.d("GoogleMap", "Clicked on map: $latlong")
                                    clickedLocation = latlong
                                }
                            },
                            uiSettings = MapUiSettings()
                        ) {
                            // Draw the polyline for the path
                            Polyline(
                                points = fitnessData.path.map { location ->
                                    LatLng(location.latitude, location.longitude)
                                },
                                color = Color.Blue,
                                width = 5f
                            )

                            // Add a marker at the starting position
                            fitnessData.path.firstOrNull()?.let { startLocation ->
                                Marker(
                                    state = MarkerState(
                                        position = LatLng(
                                            startLocation.latitude,
                                            startLocation.longitude
                                        ),
                                    ),
                                    alpha = 0.5f,
                                    title = "Starting Location",
                                    flat = false,
                                    draggable = true,
                                    snippet = "Start from here",
                                    onClick = {
                                        it.showInfoWindow()
                                        true
                                    },
                                    zIndex = 3.0f
                                )
                            }

                            // Marker for Current Location
                            fitnessData.path.lastOrNull()?.let { currentLocation ->
                                Marker(
                                    state = MarkerState(
                                        position = LatLng(
                                            currentLocation.latitude,
                                            currentLocation.longitude
                                        )
                                    ),
                                    icon = BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN
                                    ),
                                    title = "Current Location",
                                    snippet = "You are here"
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            IconButton(
                                onClick = { showMapTypeDialog = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Layers,
                                    contentDescription = "Map Type",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Change map type between all possible types
                            DropdownMenu(
                                expanded = showMapTypeDialog,
                                onDismissRequest = { showMapTypeDialog = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("NORMAL") },
                                    onClick = {
                                        mapType = MapType.NORMAL
                                        showMapTypeDialog = false
                                    },
                                    trailingIcon = {
                                        if (mapType == MapType.NORMAL)
                                            Icon(
                                                imageVector = Icons.Filled.Map,
                                                contentDescription = "Normal"
                                            )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("SATELLITE") },
                                    onClick = {
                                        mapType = MapType.SATELLITE
                                        showMapTypeDialog = false
                                    },
                                    trailingIcon = {
                                        if (mapType == MapType.SATELLITE)
                                            Icon(
                                                imageVector = Icons.Filled.SatelliteAlt,
                                                contentDescription = "Satellite"
                                            )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("HYBRID") },
                                    onClick = {
                                        mapType = MapType.HYBRID
                                        showMapTypeDialog = false
                                    },
                                    trailingIcon = {
                                        if (mapType == MapType.HYBRID)
                                            Icon(
                                                imageVector = Icons.Filled.Public,
                                                contentDescription = "Hybrid"
                                            )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("TERRAIN") },
                                    onClick = {
                                        mapType = MapType.TERRAIN // GoogleMap.MAP_TYPE_TERRAIN also available
                                        showMapTypeDialog = false
                                    },
                                    trailingIcon = {
                                        if (mapType == MapType.TERRAIN)
                                            Icon(
                                                imageVector = Icons.Filled.Terrain,
                                                contentDescription = "Terrain"
                                            )
                                    }
                                )
                            }

                            if (clickedLocation != null) {
                                Text(
                                    text = "Latitude: ${clickedLocation?.latitude}\nLongitude: ${clickedLocation?.longitude}"
                                )
                            }
                        }
                    }

                    // Show total distance
                    if (running) {
                        Text(
                            text = "Total Distance: ${"%.2f".format(fitnessData.totalDistance)} m",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = {
                            if (running) {
                                stopTracking()
                                running = false
                            } else {
                                startTracking()
                                Toast.makeText(context, "Start running", Toast.LENGTH_SHORT).show()
                                running = true
                            }
                        },
                    ) {
                        Text(text = if (running) "Stop" else "Start")
                    }
                }

                if (permanentlyDenied) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SettingsApplications,
                            contentDescription = "Permissions permanently denied",
                            modifier = Modifier.fillMaxSize()
                        )
                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                context.startActivity(intent)
                            }
                        ) {
                            Text(
                                text = "Open app settings to access features"
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MarkerFunction(
    location: LatLng,
    title: String,
    snippet: String,
    alpha: Float = 1.0f,
    zIndex: Float = 3.0f
) {
    Marker(
        state = MarkerState(
            position = LatLng(
                location.latitude,
                location.longitude
            ),
        ),
        alpha = alpha,
        title = title,
        flat = false,
        draggable = true,
        snippet = snippet,
        onClick = {
            it.showInfoWindow()
            true
        },
        zIndex = zIndex
    )
}

// Helper function to check if we should show rationale for a permission
fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
        (context as androidx.activity.ComponentActivity), permission
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun LocationScreenPreview() {
    LocationScreen(context = LocalContext.current)
}
