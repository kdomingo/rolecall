package com.academe.rolecall.login

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.academe.rolecall.R
import com.academe.rolecall.data.models.UserCredentials
import com.academe.rolecall.form.FieldErrorType
import com.academe.rolecall.main.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController = rememberNavController()) {

    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val navigateToDashboard by viewModel.navigateToDashboard.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    if (navigateToDashboard) {
        LaunchedEffect(Unit) {
            navController.navigate(Screens.Dashboard.name) {
                popUpTo(route = Screens.Login.name) {
                    inclusive = true
                }
            }
        }
    }

    state.fieldError?.let {
        val message = stringResource(it.messageRes)
        LaunchedEffect(it) {
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    state.loginError?.let {
        LaunchedEffect(it) {
            snackBarHostState.showSnackbar(message = it.message, duration = SnackbarDuration.Short)
        }
    }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { keyboardController?.hide() },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        LoginForm(
            padding = it,
            state = state,
            onTogglePasswordVisibility = viewModel::togglePasswordVisibility
        ) { creds ->
            viewModel.login(creds)
        }
    }
}

@Composable
private fun LoginForm(
    padding: PaddingValues = PaddingValues.Zero,
    state: LoginUiState = LoginUiState(),
    onTogglePasswordVisibility: () -> Unit = {},
    onSubmit: (UserCredentials) -> Unit = {}
) {

    val emailFieldState = rememberTextFieldState()
    val passwordFieldState = rememberTextFieldState()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.wrapContentWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    stringResource(R.string.login_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    state = emailFieldState,
                    isError = state.fieldError?.type == FieldErrorType.Email,
                    label = { Text(stringResource(R.string.email_label)) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutlinedSecureTextField(
                    state = passwordFieldState,
                    isError = state.fieldError?.type == FieldErrorType.Password,
                    label = { Text(stringResource(R.string.password_label)) },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    trailingIcon = {
                        Icon(
                            imageVector = when {
                                state.showPassword -> Icons.Filled.Visibility
                                else -> Icons.Filled.VisibilityOff
                            },
                            modifier = Modifier.clickable(onClick = onTogglePasswordVisibility),
                            contentDescription = stringResource(R.string.toggle_password_visibility)
                        )
                    },
                    textObfuscationMode = when {
                        state.showPassword -> TextObfuscationMode.Visible
                        else -> TextObfuscationMode.RevealLastTyped
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    enabled = !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus(force = true)
                        onSubmit.invoke(
                            UserCredentials(
                                email = emailFieldState.text.toString(),
                                password = passwordFieldState.text.toString()
                            )
                        )
                    }
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            stringResource(R.string.login_button),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginForm()
    }
}