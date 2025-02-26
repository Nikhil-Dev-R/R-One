package com.rudraksha.rone.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(
    context: Context,
    modifier: Modifier = Modifier,
    permissionGranted: Boolean = false,
    checkPermission: () -> Boolean = { true },
    fitnessData: FitnessData = FitnessData(),
    startTracking: () -> Unit = {},
    stopTracking: () -> Unit = {}
) {
    val cameraPositionState = rememberCameraPositionState()
    var running by remember { mutableStateOf(false) }
    var mapType by remember { mutableStateOf(MapType.SATELLITE) }
    var showMapTypeDialog by remember { mutableStateOf(false) }
    var mapProperties by remember {
        mutableStateOf(
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
    }
    var clickedLocation by remember { mutableStateOf<LatLng?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
//        ch
    }

    // Update camera position to the user's starting location
    LaunchedEffect(fitnessData.path) {
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

    // Focus on the user's location at the start and as they move
    LaunchedEffect(fitnessData.currentLocation) {
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Location Tracker")
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Create a search bar
                TextField(
                    value = searchText,
                    onValueChange = { value ->
                        searchText = value
                    },
                    placeholder = {
                        Text(text = "Search Location")
                    },
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
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    bounded = true,
                                    radius = 8.dp,
                                    color = Color.Cyan
                                ),
                                onClick = {
                                    showBottomSheet = true
                                }
                            ),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties,
                        onMapClick = { latlang ->
                            Log.d("GoogleMap", "Clicked on map: $latlang")
                            clickedLocation = latlang
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
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                                title = "Current Location",
                                snippet = "You are here"
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        IconButton(
                            onClick = {
                                showMapTypeDialog = true
                            }
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
                            onDismissRequest = {
                                showMapTypeDialog = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("NORMAL") },
                                onClick = {
                                    mapType = MapType.NORMAL
                                    mapProperties = mapProperties.copy(
                                        mapType = mapType
                                    )
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
                                    mapProperties = mapProperties.copy(
                                        mapType = mapType
                                    )
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
                                    mapProperties = mapProperties.copy(
                                        mapType = mapType
                                    )
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
                                    mapType =
                                        MapType.TERRAIN // GoogleMap.MAP_TYPE_TERRAIN also available
                                    mapProperties = mapProperties.copy(
                                        mapType = mapType
                                    )
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

                        if (!permissionGranted) {
                            IconButton(
                                onClick = { checkPermission() }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOff,
                                    contentDescription = "Request permission"
                                )
                            }
                        }
                    }
                }

                // Show total distance
                Text(
                    text = "Total Distance: ${"%.2f".format(fitnessData.totalDistance)} meters",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (running) {
                                stopTracking()
//                                Toast.makeText(context, "You run ${fitnessData.totalDistance}", Toast.LENGTH_SHORT).show()
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


@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun LocationScreenPreview() {
    LocationScreen(context = LocalContext.current)
}
