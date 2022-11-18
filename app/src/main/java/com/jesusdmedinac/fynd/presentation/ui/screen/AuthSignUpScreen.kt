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
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignUpBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignUpViewModel

@ExperimentalMaterial3Api
@Composable
fun AuthSignUpScreen(
    authSignUpState: AuthSignUpViewModel.State,
    authSignUpBehavior: AuthSignUpBehavior,
    onWantToSignInClick: () -> Unit,
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
                "Amar es lo que hacemos, servir es lo que somos",
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
            var emailTextFieldValue by remember { mutableStateOf(TextFieldValue(authSignUpState.email)) }
            OutlinedTextField(
                emailTextFieldValue,
                onValueChange = {
                    authSignUpBehavior.onEmailChange(it.text)
                    emailTextFieldValue = it
                },
                placeholder = {
                    Text(text = "Correo electrónico")
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if (authSignUpState.isEmailError) {
                Text(
                    "Aún no eres anfitrión o tu correo no es correcto",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            var displayNameTextFieldValue by remember { mutableStateOf(TextFieldValue(authSignUpState.displayName)) }
            OutlinedTextField(
                displayNameTextFieldValue,
                onValueChange = {
                    authSignUpBehavior.onDisplayNameChange(it.text)
                    displayNameTextFieldValue = it
                },
                placeholder = {
                    Text(text = "Nombre completo")
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if (authSignUpState.isDisplayNameError) {
                Text(
                    "¿Cómo podemos llamarte?",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue(authSignUpState.password)) }
            val visualTransformation = if (authSignUpState.isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation()
            val visibilityIcons =
                if (authSignUpState.isPasswordVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff
            OutlinedTextField(
                passwordTextFieldValue,
                onValueChange = {
                    authSignUpBehavior.onPasswordChange(it.text)
                    passwordTextFieldValue = it
                },
                placeholder = {
                    Text(text = "Contraseña")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        authSignUpBehavior.onPasswordVisibilityToggle()
                    }) {
                        Icon(visibilityIcons, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = visualTransformation,
            )
            if (authSignUpState.isPasswordError) {
                Text(
                    "La contraseña no coincide",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            var confirmPasswordTextFieldValue by remember { mutableStateOf(TextFieldValue(authSignUpState.confirmPassword)) }
            OutlinedTextField(
                confirmPasswordTextFieldValue,
                onValueChange = {
                    authSignUpBehavior.onConfirmPasswordChange(it.text)
                    confirmPasswordTextFieldValue = it
                },
                placeholder = {
                    Text(text = "Confirma tu contraseña")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        authSignUpBehavior.onPasswordVisibilityToggle()
                    }) {
                        Icon(visibilityIcons, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = visualTransformation,
            )
            if (authSignUpState.isPasswordError) {
                Text(
                    "La contraseña no coincide",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authSignUpBehavior.onSignUpClick()
            }) {
                if (authSignUpState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,

                        )
                } else {
                    Text("Registrarme")
                }
            }
            if (authSignUpState.signUpErrorMessage.isNotEmpty()) {
                Text(
                    authSignUpState.signUpErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            TextButton(onClick = {
                onWantToSignInClick()
            }) {
                Text("Ya soy voluntario")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun SignUpScreenPreview() {
    FyndTheme {
        AuthSignUpScreen(
            authSignUpState = AuthSignUpViewModel.State(),
            authSignUpBehavior = object : AuthSignUpBehavior {
                override fun onEmailChange(email: String) {
                    TODO("Not yet implemented")
                }

                override fun onDisplayNameChange(displayName: String) {
                    TODO("Not yet implemented")
                }

                override fun onPasswordChange(password: String) {
                    TODO("Not yet implemented")
                }

                override fun onConfirmPasswordChange(confirmPassword: String) {
                    TODO("Not yet implemented")
                }

                override fun onPasswordVisibilityToggle() {
                    TODO("Not yet implemented")
                }

                override fun onSignUpClick() {
                    TODO("Not yet implemented")
                }
            },
            onWantToSignInClick = {}
        )
    }
}