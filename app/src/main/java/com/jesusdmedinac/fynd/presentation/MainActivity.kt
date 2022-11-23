package com.jesusdmedinac.fynd.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.jesusdmedinac.fynd.presentation.ui.FyndApp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.MainActivityViewModel
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            mainActivityViewModel.onCodeScanned(result.contents)
        }
    }

    private fun launchScanner() {
        val scanOptions = ScanOptions().apply {
            setOrientationLocked(false)
            setBeepEnabled(false)
        }
        barcodeLauncher.launch(scanOptions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FyndTheme(
                darkTheme = true,
            ) {
                FyndApp(
                    launchScanner = ::launchScanner,
                    mainActivityViewModel,
                )
            }
        }
    }
}
