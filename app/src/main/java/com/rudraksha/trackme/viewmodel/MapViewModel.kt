package com.rudraksha.trackme.viewmodel

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

    @OptIn(ExperimentalPermissionsApi::class)
    fun checkPermissions( permissionStates: MultiplePermissionsState ) {
        viewModelScope.launch {
            var allGranted = permissionStates.permissions.all { permission ->
                permission.status.isGranted
            }

            if (!allGranted) {
                permissionStates.permissions.forEach { permission ->
                    if (permission.status.shouldShowRationale) {
                        _uiState.update { it.copy(permissionsDeniedPermanently = true) }
                        return@forEach
                    }
                    if (!permission.status.isGranted) {
                        permission.launchPermissionRequest()
                    }
                }
                allGranted = permissionStates.permissions.all { permission ->
                    permission.status.isGranted
                }
            }

            _uiState.update {
                it.copy(permissionsGranted = allGranted)
            }
        }
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private var lastLocation: Location? = null
    private var isTracking = false

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val newLocation = locationResult.lastLocation ?: return

            if (lastLocation != null) {
                val newDistance = lastLocation?.distanceTo(newLocation) ?: 0f

                if (newDistance > 3f) {
                    _uiState.update {
                        UiState (
                            fitnessData = FitnessData(
                                path = it.fitnessData.path + newLocation,
                                totalDistance = it.fitnessData.totalDistance + newDistance,
                                currentLocation = newLocation
                            )
                        )
                    }
                }
            }
            lastLocation = newLocation
        }
    }

    @SuppressLint("MissingPermission")
    fun startTracking() {
        if (isTracking) return

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // Update every 1 second
        ).build()

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { currentLocation ->
                lastLocation = currentLocation

                _uiState.update {
                    UiState(
                        fitnessData = FitnessData(
                            path = it.fitnessData.path + currentLocation,
                            totalDistance = it.fitnessData.totalDistance,
                            currentLocation = currentLocation
                        )
                    )
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun stopTracking() {
        if (!isTracking) return
        isTracking = false
        fusedLocationClient.removeLocationUpdates(locationCallback)
        lastLocation = null
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
    var fitnessData: FitnessData = FitnessData(),
    val permissionsGranted: Boolean = false,
    val permissionsDeniedPermanently: Boolean = false,
)
