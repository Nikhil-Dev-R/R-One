package com.rudraksha.rone.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.launch

data class FitnessData(
    val path: List<Location> = emptyList(),
    val totalDistance: Float = 0f,
    val currentLocation: Location? = null
)

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private var lastLocation: Location? = null
    private var isTracking = false

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            viewModelScope.launch {
                val newLocation = locationResult.lastLocation ?: return@launch

                if (lastLocation != null) {
                    val newDistance = lastLocation?.distanceTo(newLocation) ?: 0f

                    if (newDistance > 3f) {
                        _uiState.update {
                            UiState(
                                mapData = FitnessData(
                                    path = it.mapData.path + newLocation,
                                    totalDistance = it.mapData.totalDistance + newDistance,
                                    currentLocation = newLocation
                                )
                            )
                        }
                    }
                }
                lastLocation = newLocation
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startTracking() {
        viewModelScope.launch {
            if (isTracking) return@launch

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000L // Update every 1 second
            ).build()

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let { currentLocation ->
                    lastLocation = currentLocation

                    _uiState.update {
                        UiState(
                            mapData = FitnessData(
                                path = it.mapData.path + currentLocation,
                                totalDistance = it.mapData.totalDistance,
                                currentLocation = currentLocation
                            )
                        )
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun stopTracking() {
        viewModelScope.launch {
            if (!isTracking) return@launch
            isTracking = false
            fusedLocationClient.removeLocationUpdates(locationCallback)
            lastLocation = null
        }
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
                        return MapViewModel(context.applicationContext as Application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

data class UiState(
    var mapData: FitnessData = FitnessData(),
)
