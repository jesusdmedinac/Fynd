package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignInBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignInViewModel

@ExperimentalMaterial3Api
@Composable
fun AuthSignInScreen(
    authSignInState: AuthSignInViewModel.State,
    authSignInBehavior: AuthSignInBehavior,
    onWantToSignUpClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(32.dp),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "¿Listo para empezar un nuevo día?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Ingresa tu correo y contraseña",
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            var emailTextFieldValue by remember { mutableStateOf(TextFieldValue(authSignInState.email)) }
            OutlinedTextField(
                emailTextFieldValue,
                onValueChange = {
                    authSignInBehavior.onEmailChange(it.text)
                    emailTextFieldValue = it
                },
                placeholder = {
                    Text(text = "Correo electrónico")
                },
                modifier = Modifier.fillMaxWidth(),
                isError = authSignInState.isEmailError,
            )
            if (authSignInState.isEmailError) {
                Text(
                    "Aún no eres anfitrión o tu correo o contraseña no es correcto",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            val visualTransformation = if (authSignInState.isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation()
            var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue(authSignInState.password)) }
            OutlinedTextField(
                passwordTextFieldValue,
                onValueChange = {
                    authSignInBehavior.onPasswordChange(it.text)
                    passwordTextFieldValue = it
                },
                placeholder = {
                    Text(text = "Contraseña")
                },
                trailingIcon = {
                    IconButton(onClick = authSignInBehavior::onPasswordVisibilityToggle) {
                        val visibilityIcons =
                            if (authSignInState.isPasswordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff
                        Icon(visibilityIcons, contentDescription = null)
                    }
                },
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth(),
                isError = authSignInState.isPasswordError,
            )
            if (authSignInState.isPasswordError) {
                Text(
                    "Aún no eres anfitrión o tu correo o contraseña no es correcto",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = authSignInBehavior::onLoginClick) {
                if (authSignInState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }
            if (authSignInState.signInErrorMessage.isNotEmpty()) {
                Text(
                    authSignInState.signInErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            TextButton(onClick = {
                onWantToSignUpClick()
            }) {
                Text("¡Quiero ser voluntario!")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun LoginScreenPreview() {
    FyndTheme {
        AuthSignInScreen(
            authSignInState = AuthSignInViewModel.State(
                email = "email",
                password = "password",
                isLoading = true,
            ),
            authSignInBehavior = object : AuthSignInBehavior {
                override fun onEmailChange(email: String) {
                    TODO("Not yet implemented")
                }

                override fun onPasswordChange(password: String) {
                    TODO("Not yet implemented")
                }

                override fun onPasswordVisibilityToggle() {
                    TODO("Not yet implemented")
                }

                override fun onLoginClick() {
                    TODO("Not yet implemented")
                }
            },
            onWantToSignUpClick = {}
        )
    }
}