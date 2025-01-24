package org.tidy.feature_auth.presentation.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRoot(
    viewModel: AuthViewModel = koinViewModel(),
    onNavigateToHome: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state =  viewModel.state.collectAsState()

    LaunchedEffect(state.value.isAuthenticated) {
        if (state.value.isAuthenticated) {
            onNavigateToHome(state.value.userEmail ?: "")
        } else {
            onNavigateToLogin()
        }
    }
  AuthScreen()


}

@Composable
fun AuthScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}