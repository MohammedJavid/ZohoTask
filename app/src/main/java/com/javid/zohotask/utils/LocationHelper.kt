package com.javid.zohotask.utils
//
//import android.annotation.SuppressLint
//import android.location.Location
//import android.os.Looper
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.common.api.ResolvableApiException
//import com.google.android.gms.location.*
//import com.javid.zohotask.MainActivity
//import java.text.DateFormat
//import java.util.*
//
//
//@SuppressLint("StaticFieldLeak")
//class LocationHelper {
//    companion object {
//        private var mFusedLocationClient: FusedLocationProviderClient? = null
//        private lateinit var mLocationSettingsRequest: LocationSettingsRequest
//        private lateinit var mLocationCallback: LocationCallback
//        private lateinit var mLocationRequest: LocationRequest
//        private var mSettingsClient: SettingsClient? = null
//        private var mCurrentLocation: Location? = null
//        private var mLastUpdateTime: String? = null
//
//        fun initializeLocation() {
//            MainActivity.mainActivity?.let {
//                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
//                mSettingsClient = LocationServices.getSettingsClient(it)
//
//                mLocationCallback = object : LocationCallback() {
//                    override fun onLocationResult(locationResult: LocationResult) {
//                        super.onLocationResult(locationResult)
//                        locationResult.lastLocation.let { location ->
//                            mCurrentLocation = location
//                            MainActivity.locationData.value = location
//                            mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
//                        }
//                    }
//                }
//                mLocationRequest = LocationRequest()
//                mLocationRequest.interval = 10000
//                mLocationRequest.fastestInterval = 5000
//                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//
//                val builder = LocationSettingsRequest.Builder()
//                builder.addLocationRequest(mLocationRequest)
//                mLocationSettingsRequest = builder.build()
//            }
//            startLocationUpdates()
//        }
//
//        private fun startLocationUpdates() {
//            mSettingsClient?.let { settings ->
//                settings
//                    .checkLocationSettings(mLocationSettingsRequest)
//                    .addOnSuccessListener {
//                        MainActivity.gpsState.value = true
//                        mFusedLocationClient?.requestLocationUpdates(
//                            mLocationRequest,
//                            mLocationCallback, Looper.getMainLooper()
//                        )
//                    }
//                    .addOnFailureListener {
//                        MainActivity.gpsState.value = false
//                        when ((it as ApiException).statusCode) {
//                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
//                                val rae: ResolvableApiException = (it as ResolvableApiException)
//                                MainActivity.mainActivity?.let { it1 ->
//                                    rae.startResolutionForResult(
//                                        it1,
//                                        MainActivity.REQUESTCHECKSETTINGS
//                                    )
//                                }
//                            }
//                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                                MainActivity.mainActivity?.settingsChangeUnavailable()
//                            }
//                        }
//                    }
//            }
//        }
//
//        fun stopLocationUpdates() {
//            mFusedLocationClient?.let { fuse ->
//                fuse
//                    .removeLocationUpdates(mLocationCallback)
//                    .addOnCompleteListener {
//                    }
//            }
//            mFusedLocationClient = null
//            mSettingsClient = null
//        }
//
//    }
//}