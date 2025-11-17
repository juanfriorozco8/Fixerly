package uvg.plats.fixerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Auth screens
import uvg.plats.fixerly.ui.screens.auth.OnboardingScreen
import uvg.plats.fixerly.ui.screens.auth.LoginScreen
import uvg.plats.fixerly.ui.screens.auth.RegisterScreen
import uvg.plats.fixerly.ui.screens.auth.AccountTypeScreen
import uvg.plats.fixerly.ui.screens.auth.AddressScreen
import uvg.plats.fixerly.ui.screens.auth.SupplierDataScreen

// Client screens
import uvg.plats.fixerly.ui.screens.client.LaborScreen
import uvg.plats.fixerly.ui.screens.client.YourRequestsScreen
import uvg.plats.fixerly.ui.screens.client.UserProfileScreen

// Supplier screens
import uvg.plats.fixerly.ui.screens.supplier.SupplierWelcomeScreen
import uvg.plats.fixerly.ui.screens.supplier.SupplierProfileScreen

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingDestination
    ) {

        // ONBOARDING
        composable<OnboardingDestination> {
            OnboardingScreen(
                onNavigateToLogin = { navController.navigate(LoginDestination) },
                onNavigateToRegister = { navController.navigate(RegisterDestination) }
            )
        }

        // LOGIN
        composable<LoginDestination> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(RegisterDestination)
                },
                onLoginSuccess = { accountType ->
                    if (accountType == "Cliente") {
                        navController.navigate(LaborDestination) {
                            popUpTo(OnboardingDestination) { inclusive = true }
                        }
                    } else {
                        navController.navigate(SupplierWelcomeDestination) {
                            popUpTo(OnboardingDestination) { inclusive = true }
                        }
                    }
                }
            )
        }

        // REGISTRO
        composable<RegisterDestination> {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginDestination) {
                        popUpTo(RegisterDestination) { inclusive = true }
                    }
                },
                onNavigateToAccountType = {
                    navController.navigate(AccountTypeDestination)
                }
            )
        }

        // TIPO DE CUENTA
        composable<AccountTypeDestination> {
            AccountTypeScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginDestination)
                },
                onNavigateToNext = { accountType ->
                    if (accountType == "Proveedor") {
                        // No necesitamos leer el parámetro en la pantalla,
                        // solo navegamos a esa ruta.
                        navController.navigate(SupplierDataDestination(accountType))
                    } else {
                        navController.navigate(AddressDestination)
                    }
                }
            )
        }

        // DIRECCIÓN DEL CLIENTE
        composable<AddressDestination> {
            AddressScreen(
                onComplete = {
                    navController.navigate(LaborDestination) {
                        popUpTo(OnboardingDestination) { inclusive = true }
                    }
                }
            )
        }

        // DATOS DEL PROVEEDOR
        composable<SupplierDataDestination> {
            SupplierDataScreen(
                onComplete = {
                    navController.navigate(SupplierWelcomeDestination) {
                        popUpTo(OnboardingDestination) { inclusive = true }
                    }
                }
            )
        }

        // ==================== CLIENTE ====================

        composable<LaborDestination> {
            LaborScreen()
        }

        composable<YourRequestsDestination> {
            YourRequestsScreen()
        }

        composable<UserProfileDestination> {
            UserProfileScreen()
        }

        // ==================== PROVEEDOR ====================

        composable<SupplierWelcomeDestination> {
            SupplierWelcomeScreen()
        }

        composable<SupplierProfileDestination> {
            SupplierProfileScreen()
        }
    }
}
