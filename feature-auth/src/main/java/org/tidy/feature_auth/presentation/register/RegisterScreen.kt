    package org.tidy.feature_auth.presentation.register


    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.input.PasswordVisualTransformation
    import androidx.compose.ui.unit.dp
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.launch
    import org.koin.androidx.compose.koinViewModel
    import org.tidy.core_ui.theme.primaryLight


    @Composable
    fun RegisterRoot(
        modifier: Modifier,
        viewModel: RegisterViewModel = koinViewModel(),
        onNavigateToLogin: () -> Unit
    ) {
        val state by viewModel.state.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(state.isRegistered) {
            if (state.isRegistered) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Cadastro realizado com sucesso!")
                    delay(2000)
                    onNavigateToLogin()
                }
            }
        }

        LaunchedEffect(state.errorMessage) {
            state.errorMessage?.let { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            RegisterScreen(
                modifier = modifier.padding(paddingValues),
                state = state,
                onAction = { action -> viewModel.onAction(action) },
                onNavigateToLogin = onNavigateToLogin
            )
        }
    }
    @Composable
    fun RegisterScreen(
        modifier: Modifier = Modifier,
        state: RegisterState,
        onAction: (RegisterAction) -> Unit,
        onNavigateToLogin: () -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Criar Conta", style = MaterialTheme.typography.headlineMedium, color = primaryLight)

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de E-mail
            OutlinedTextField(
                value = state.email,
                onValueChange = { onAction(RegisterAction.OnEmailChange(it)) },
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
                onValueChange = { onAction(RegisterAction.OnPasswordChange(it)) },
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

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Confirmação de Senha
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { onAction(RegisterAction.OnConfirmPasswordChange(it)) },
                label = { Text("Confirmar Senha") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = state.confirmPassword.isBlank() || state.password != state.confirmPassword
            )
            if (state.confirmPassword.isBlank()) {
                Text(
                    text = "A confirmação de senha é obrigatória",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else if (state.password != state.confirmPassword) {
                Text(
                    text = "As senhas não coincidem",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão de Registrar
            Button(
                onClick = { onAction(RegisterAction.OnRegisterClick) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && state.email.isNotBlank() && state.password.isNotBlank() && state.confirmPassword.isNotBlank() && state.password == state.confirmPassword && state.password.length >= 6
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Link "Já tem uma conta? Faça login"
            TextButton(onClick = onNavigateToLogin) {
                Text("Já tem uma conta? Faça login", color = primaryLight)
            }
        }
    }