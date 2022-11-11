package com.jesusdmedinac.fynd.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { firebaseAuthUIAuthenticationResult ->
        mainActivityViewModel.behavior.onSignInResult(firebaseAuthUIAuthenticationResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FyndTheme {
                FyndApp(
                    mainScreenViewModel,
                    qrScreenViewModel,
                    placesScreenViewModel,
                    launchScanner = {
                        val scanOptions = ScanOptions().apply {
                            setOrientationLocked(false)
                            setBeepEnabled(false)
                        }
                        barcodeLauncher.launch(scanOptions)
                    },
                    launchSignIn = {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("892734723976-mcbdc35aotmhavflv34apnjqdh1ea67c.apps.googleusercontent.com")
                            .requestEmail()
                            .build()

                        val googleSignInClient = GoogleSignIn.getClient(this, gso)
                        signInLauncher.launch(googleSignInClient.signInIntent)
                    }
                )
            }
        }
    }
}
