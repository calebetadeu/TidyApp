package org.tidy.feature_auth.presentation.login

import LoginViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.tidy.core_ui.theme.primaryLight

@Composable
fun LoginRoot(
    modifier: Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            coroutineScope.launch {
             //   snackBarHostState.showSnackbar("Login realizado com sucesso!")
                onNavigateToHome(state.email)
            }
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        LoginScreen(
            modifier = modifier.padding(paddingValues),
            state = state,
            onAction = { action -> viewModel.onAction(action) },
            onNavigateToRegister = onNavigateToRegister
        )
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium, color = primaryLight)

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de E-mail
        OutlinedTextField(
            value = state.email,
            onValueChange = { onAction(LoginAction.OnEmailChange(it)) },
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = state.email.isBlank()
        )
        if (state.email.isBlank()) {
            Text(
                text = "O e-mail é obrigatório",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Senha
        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
            label = { Text("Senha") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = state.password.isBlank() || state.password.length < 6
        )
        if (state.password.isBlank()) {
            Text(
                text = "A senha é obrigatória",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else if (state.password.length < 6) {
            Text(
                text = "A senha deve ter pelo menos 6 caracteres",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botão de Login
        Button(
            onClick = { onAction(LoginAction.OnLoginClick) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading && state.email.isNotBlank() && state.password.isNotBlank() && state.password.length >= 6
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Entrar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Link "Criar conta"
        TextButton(onClick = onNavigateToRegister) {
            Text("Não tem uma conta? Cadastre-se", color = primaryLight)
        }
    }
}