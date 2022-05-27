package com.javid.zohotask.ui.phase2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.javid.zohotask.BuildConfig
import com.javid.zohotask.databinding.FragmentPhase2Binding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Phase2Fragment : Fragment() {

    private lateinit var binding: FragmentPhase2Binding
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var geocoder: Geocoder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhase2Binding.inflate(inflater, container, false)

        requestLocationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    getCurrentLocation()
                    Log.d( "onCreateView: permission1", true.toString())
                } else {
                    Log.d( "onCreateView: permission2", false.toString())
                    requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    private fun checkPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun requestPermissions(permission: String) {
        val displayRational = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission
            )
        if (!displayRational) {
            Toast.makeText(
                requireActivity(),
                "Location permissions required",
                Toast.LENGTH_LONG
            ).show()

            //UI Functionalities

//            val intent = Intent()
//            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//            val uri = Uri.fromParts(
//                "package",
//                BuildConfig.APPLICATION_ID, null
//            )
//            intent.data = uri
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)

        } else {
            requestLocationPermissionLauncher.launch(permission)
        }
    }


    private fun getCurrentLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            geocoder = Geocoder(requireActivity(), Locale.getDefault())
            val addresses = geocoder?.getFromLocation(it.latitude, it.longitude, 1)
            val city = addresses?.get(0)?.locality
        }
    }

}