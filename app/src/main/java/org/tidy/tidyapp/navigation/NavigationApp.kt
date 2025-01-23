package org.tidy.tidyapp.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import org.tidy.feature_auth.presentation.login.LoginRoot
import org.tidy.feature_auth.presentation.register.RegisterRoot

@Composable

fun NavigationApp(
    modifier: Modifier = Modifier
) {
    TidyAppTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.Login
        ) {
            composable<Route.Login>(
//                exitTransition = { slideOutHorizontally() },
//                popEnterTransition = { slideInHorizontally() }
            ) {
                LoginRoot(
                    onNavigateToHome = { email ->
                        navController.navigate(Route.Home(email = email))
                    },
                    onNavigateToRegister = {
                        navController.navigate(Route.Register)
                    },
                    modifier =modifier,
                )
            }
            composable<Route.Register>(
//                exitTransition = { slideOutHorizontally() },
//                popEnterTransition = { slideInHorizontally() }
            ) {
                RegisterRoot(
                    onNavigateToLogin = {
                        navController.navigate(Route.Login)
                    },
                    modifier = modifier
                )
            }
            composable<Route.Home>(
//                exitTransition = { slideOutHorizontally() },
//                popEnterTransition = { slideInHorizontally() }
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Bem-vindo,")
                }
            }

        }


    }

}