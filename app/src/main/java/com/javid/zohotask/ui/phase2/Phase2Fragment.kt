package com.javid.zohotask.ui.phase2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.javid.zohotask.BuildConfig
import com.javid.zohotask.R
import com.javid.zohotask.databinding.FragmentPhase2Binding
import com.javid.zohotask.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class Phase2Fragment : Fragment() {

    private lateinit var binding: FragmentPhase2Binding
    private val phase2ViewModel: Phase2ViewModel by viewModels()
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var geocoder: Geocoder? = null
    private var settingsClient: SettingsClient? = null
    private lateinit var locationSetting: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var locationPermissionSetting: ActivityResultLauncher<Intent>
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private var gpsState = MutableLiveData<Boolean?>()
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhase2Binding.inflate(inflater, container, false)
        count = 0
        binding.clMcViewData.visibility = View.GONE
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        settingsClient = LocationServices.getSettingsClient(requireActivity())

        requestLocationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    getCurrentLocation()
                } else {
                    requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

        locationSetting = registerForActivityResult(StartIntentSenderForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    setObservers()
                    lifecycleScope.launch {
                        binding.clMcViewProgressBar.visibility = View.VISIBLE
                        binding.btnGpsError.isClickable = false
                        delay(5000)
                        gpsState.value = true
                    }

                }
                Activity.RESULT_CANCELED -> {
                    gpsState.value = false
                }
            }
        }

        locationPermissionSetting =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    Activity.RESULT_OK -> {
                        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                    Activity.RESULT_CANCELED -> {
                        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }

        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        setObservers()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnRandomWeather.setOnClickListener {
            findNavController().navigate(Phase2FragmentDirections.actionPhase2FragmentToRandomLocationWeatherFragment())
        }
    }

    private fun requestPermissions(permission: String) {
        val displayRational = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            permission
        )
        if (!displayRational) {
            Toast.makeText(
                requireActivity(),
                getString(R.string.location_permission_required),
                Toast.LENGTH_LONG
            ).show()

            binding.clMcViewGpsError.visibility = View.VISIBLE
            binding.clMcViewData.visibility = View.GONE
            binding.tvMcViewGpsErrorText.text = getString(R.string.location_permission_required)
            binding.btnGpsError.isClickable = true
            binding.btnGpsError.text = getString(R.string.enable_gps_permission)

            binding.btnGpsError.setOnClickListener {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(
                    "package",
                    BuildConfig.APPLICATION_ID, null
                )
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                locationPermissionSetting.launch(intent)
            }
        } else {
            requestLocationPermissionLauncher.launch(permission)
        }
    }

    private fun getCurrentLocation() {
        settingsClient?.let { settings ->
            locationRequest = LocationRequest.create()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 5000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest)
            locationSettingsRequest = builder.build()
            settings
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    gpsState.value = true
                }
                .addOnFailureListener {
                    gpsState.value = false
                    when ((it as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            val intentSenderRequest =
                                IntentSenderRequest.Builder((it as ResolvableApiException).resolution)
                                    .build()
                            locationSetting.launch(intentSenderRequest)
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                }
        }

    }

    private fun setObservers() {

        phase2ViewModel.weatherData.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.clMcViewProgressBar.visibility = View.VISIBLE
                        binding.clMcViewData.visibility = View.GONE
                        binding.clMcViewError.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                        if (it.data != null) {
                            val weatherData = it.data
                            binding.tvTemperatureText.text = weatherData.current?.tempC.toString().plus("\u2103")
                            binding.tvDateText.text = weatherData.current?.lastUpdated?.let { it1 ->
                                getFormattedDate(
                                    it1
                                )
                            }
                            binding.tvHumidityText.text = weatherData.current?.humidity.toString().plus("%")
                            binding.tvWindText.text = weatherData.current?.windKph.toString().plus("Kmph")
                            binding.tvConditionText.text = weatherData.current?.condition?.text
                            binding.tvAirQualityText.text = when(weatherData.current?.airQuality?.usEpaIndex) {
                                1 -> { getString(R.string.good) }
                                2 -> { getString(R.string.moderate) }
                                3,4,5 -> { getString(R.string.unhealthy) }
                                else -> { getString(R.string.hazardous) }
                            }
                            when {
                                weatherData.current?.condition?.text?.lowercase()?.contains("cloud") == true -> {
                                    binding.ivWeatherCondition
                                        .setImageDrawable(
                                            ContextCompat.getDrawable(
                                                requireActivity(),R.drawable.cloudy))
                                }
                                weatherData.current?.condition?.text?.lowercase()?.contains("mist") == true -> {
                                    binding.ivWeatherCondition
                                        .setImageDrawable(
                                            ContextCompat.getDrawable(
                                                requireActivity(),R.drawable.mist))
                                }
                                weatherData.current?.condition?.text?.lowercase()?.contains("rain") == true -> {
                                    binding.ivWeatherCondition
                                        .setImageDrawable(
                                            ContextCompat.getDrawable(
                                                requireActivity(),R.drawable.rainy))
                                }
                                weatherData.current?.condition?.text?.lowercase()?.contains("sunny") == true -> {
                                    binding.ivWeatherCondition
                                        .setImageDrawable(
                                            ContextCompat.getDrawable(
                                                requireActivity(),R.drawable.sunny))
                                }
                                else -> {
                                    binding.ivWeatherCondition.setImageDrawable(null)
                                }
                            }
                            weatherData.current?.condition?.icon?.let { it1 ->
                                Glide.with(requireActivity())
                                    .load("https:".plus(it1))
                                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .error(com.google.android.material.R.drawable.mtrl_ic_error)
                                    .into(binding.ivCondition)
                            }
                            binding.clMcViewProgressBar.visibility = View.GONE
                            binding.clMcViewData.visibility = View.VISIBLE
                            binding.clMcViewError.visibility = View.GONE
                        }
                    }
                    Status.ERROR -> {
                        binding.clMcViewProgressBar.visibility = View.GONE
                        binding.clMcViewData.visibility = View.GONE
                        binding.tvMcViewErrorText.text = it.message
                        binding.clMcViewError.visibility = View.VISIBLE
                    }
                }
            }
        }

        gpsState.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    getLocation()
                    binding.clMcViewProgressBar.visibility = View.GONE
                    binding.clMcViewGpsError.visibility = View.GONE
                    binding.clMcViewData.visibility = View.VISIBLE
                } else {
                    binding.tvMcViewGpsErrorText.text = "Location turned OFF.\nPlease turn On your location"

                    binding.btnGpsError.isClickable = true
                    binding.btnGpsError.text = getString(R.string.turn_on_gps)
                    binding.btnGpsError.setOnClickListener {
                        settingsClient?.let { settings ->
                            locationRequest = LocationRequest.create()
                            locationRequest.interval = 5000
                            locationRequest.fastestInterval = 2000
                            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            val builder = LocationSettingsRequest.Builder()
                            builder.addLocationRequest(locationRequest)
                            locationSettingsRequest = builder.build()
                            settings
                                .checkLocationSettings(locationSettingsRequest)
                                .addOnSuccessListener { }
                                .addOnFailureListener { it ->
                                    when ((it as ApiException).statusCode) {
                                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                            val intentSenderRequest =
                                                IntentSenderRequest.Builder((it as ResolvableApiException).resolution)
                                                    .build()
                                            locationSetting.launch(intentSenderRequest)
                                        }
                                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                                    }
                                }
                        }
                    }
                    binding.clMcViewGpsError.visibility = View.VISIBLE
                    binding.clMcViewData.visibility = View.GONE
                }
            }
        }
    }

    private fun getLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            try {
                geocoder = Geocoder(requireActivity(), Locale.getDefault())
                val addresses = geocoder?.getFromLocation(it.latitude, it.longitude, 1)
                val city = addresses?.get(0)?.locality

                city?.let { it1 ->
                    binding.tvLocationText.text = it1
                    phase2ViewModel.getWeatherData(
                        url = getString(R.string.weather_url),
                        city = it1
                    )
                }
            } catch (exception: Exception) {
                Toast.makeText(requireActivity(), "Error: Restart app again", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getFormattedDate(timeStamp: String): String {
        return try {
            val apiFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val  uiFormat = SimpleDateFormat("dd MMM yyyy hh:mm a")
            uiFormat.format(
                Date(
                    apiFormat.parse(timeStamp)!!.time
                )
            )
        } catch (e: Exception) {
            "-"
        }
    }

}