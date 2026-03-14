package com.academe.rolecall.login

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch

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
            navController.navigate(Screens.Dashboard.name)
        }
    }

    state.fieldError?.let {
        val message = stringResource(it.messageRes)
        LaunchedEffect(it) {
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { keyboardController?.hide() },
        containerColor = Color.White,
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
                fontWeight = FontWeight.W600,
                fontSize = 28.sp
            )
            OutlinedTextField(
                state = emailFieldState,
                isError = state.fieldError?.type == FieldErrorType.Email,
                label = { Text(stringResource(R.string.email_label)) }
            )
            OutlinedSecureTextField(
                state = passwordFieldState,
                isError = state.fieldError?.type == FieldErrorType.Password,
                label = { Text(stringResource(R.string.password_label)) },
                trailingIcon = {
                    Icon(
                        imageVector = when {
                            state.showPassword -> Icons.Filled.Visibility
                            else -> Icons.Filled.VisibilityOff
                        },
                        modifier = Modifier.clickable(onClick = onTogglePasswordVisibility::invoke::invoke),
                        contentDescription = stringResource(R.string.toggle_password_visibility)
                    )
                },
                textObfuscationMode = when {
                    state.showPassword -> TextObfuscationMode.Visible
                    else -> TextObfuscationMode.RevealLastTyped
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.End),
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
                Text(stringResource(R.string.login_button))
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    Scaffold(
        containerColor = Color.White
    ) {
        LoginForm(it) { }
    }
}