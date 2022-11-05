package com.jesusdmedinac.fynd.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.enableSavedStateHandles
import com.jesusdmedinac.fynd.main.presentation.ui.FyndApp
import com.jesusdmedinac.fynd.main.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.main.presentation.viewmodel.MainActivityViewModel
import com.jesusdmedinac.fynd.main.presentation.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.onboarding.presentation.viewmodel.QRScreenViewModel
import com.jesusdmedinac.fynd.places.presentation.viewmodel.PlacesScreenViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val qrScreenViewModel: QRScreenViewModel by viewModels()
    private val placesScreenViewModel: PlacesScreenViewModel by viewModels()

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            mainActivityViewModel.behavior.onCodeScanned(result.contents)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FyndTheme {
                FyndApp(
                    mainScreenViewModel,
                    qrScreenViewModel,
                    placesScreenViewModel,
                ) {
                    val scanOptions = ScanOptions().apply {
                        setOrientationLocked(false)
                        setBeepEnabled(false)
                    }
                    barcodeLauncher.launch(scanOptions)
                }
            }
        }
    }
}
