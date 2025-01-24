package org.tidy.tidyapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.tidy.core_ui.theme.TidyAppTheme
import org.tidy.feature_auth.presentation.auth.AuthRoot
import org.tidy.feature_auth.presentation.login.LoginRoot
import org.tidy.feature_auth.presentation.register.RegisterRoot
import org.tidy.tidyapp.presentation.HomeScreen

@Composable
fun NavigationApp(
    modifier: Modifier = Modifier
) {
    TidyAppTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Route.Auth
        ) {
            composable<Route.Auth> {
                AuthRoot(
                    onNavigateToHome = { email ->
                        navController.navigate(Route.Home(email))
                    },
                    onNavigateToLogin = {
                        navController.navigate(Route.Login)
                    }
                )
            }

            // 📌 Tela de Login
            composable<Route.Login>(
//                exitTransition = { slideOutHorizontally() },
//                popEnterTransition = { slideInHorizontally() }
            ) {
                LoginRoot(
                    onNavigateToHome = { email ->
                        navController.navigate(Route.Home(email)) {
                            popUpTo(Route.Auth) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Route.Register)
                    },
                    modifier = modifier
                )
            }

            // 📌 Tela de Registro
            composable<Route.Register>(
//                exitTransition = { slideOutHorizontally() },
//                popEnterTransition = { slideInHorizontally() }
            ) {
                RegisterRoot(
                    onNavigateToLogin = {
                        navController.navigate(Route.Login) {
                            popUpTo(Route.Auth) { inclusive = true }
                        }
                    },
                    modifier = modifier
                )
            }

            // 📌 Tela Home
            composable<Route.Home>(
//                exitTransition = { slideOutHorizontally() },
//                popEnterTransition = { slideInHorizontally() }
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: "Usuário"

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Bem-vindo, $email")
                }
            }
            composable<Route.Home>(
            ) {
                HomeScreen (
                    onNavigateToClients = { navController.navigate(Route.Clients) },
                    onNavigateToBilling = { navController.navigate(Route.Billing) },
                    onNavigateToUpdates = { navController.navigate(Route.Updates) },
                    onNavigateToPlanning = { navController.navigate(Route.Planning) },
                    userEmail = "teste"
                )
            }
        }
    }
}