package com.javid.zohotask

import android.app.AlertDialog
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.javid.zohotask.databinding.ActivityMainBinding
import com.javid.zohotask.databinding.LayoutNoIternetDialogBinding
import com.javid.zohotask.ui.phase1.main.Phase1ViewModel
import com.javid.zohotask.utils.ConnectivityCheck
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var connectivityCheck: ConnectivityCheck? = null
    private var noInternetDialog: AlertDialog?  = null
    private val phase1ViewModel: Phase1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        createNoInternetDialog()
        connectivityCheck = ConnectivityCheck(object : ConnectivityCheck.ConnectivityCallback {
            override fun turnedOn() {
                hasInternet = true
                noInternetDialog?.dismiss()
            }

            override fun turnedOff() {
                hasInternet = false
                noInternetDialog?.show()
            }
        })
        registerReceiver(connectivityCheck, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    companion object {
        var hasInternet = false
    }

    private fun createNoInternetDialog() {
        val dialogBinding = LayoutNoIternetDialogBinding.inflate(
            LayoutInflater.from(this)
        )
        noInternetDialog = AlertDialog.Builder(this, R.style.dialog_style)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()
    }

    override fun onDestroy() {
        phase1ViewModel.deleteAll()
        super.onDestroy()
    }

}