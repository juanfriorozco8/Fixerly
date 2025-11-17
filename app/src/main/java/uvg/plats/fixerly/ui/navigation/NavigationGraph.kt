package uvg.plats.fixerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

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

        // -------------------------
        // ONBOARDING
        // -------------------------
        composable<OnboardingDestination> {
            OnboardingScreen(
                onNavigateToLogin = { navController.navigate(LoginDestination) },
                onNavigateToRegister = { navController.navigate(RegisterDestination) }
            )
        }

        // -------------------------
        // LOGIN
        // -------------------------
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


        // -------------------------
        // REGISTRO
        // -------------------------
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

        // -------------------------
        // TIPO DE CUENTA
        // -------------------------
        composable<AccountTypeDestination> {
            AccountTypeScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginDestination)
                },
                onNavigateToNext = { accountType ->
                    if (accountType == "Proveedor") {
                        navController.navigate(SupplierDataDestination(accountType))
                    } else {
                        navController.navigate(AddressDestination)
                    }
                }
            )
        }

        // -------------------------
        // DIRECCIÓN DEL CLIENTE
        // -------------------------
        composable<AddressDestination> {
            AddressScreen(
                onComplete = {
                    navController.navigate(LaborDestination) {
                        popUpTo(OnboardingDestination) { inclusive = true }
                    }
                }
            )
        }

        // -------------------------
        // DATOS DEL PROVEEDOR
        // -------------------------
        composable<SupplierDataDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<SupplierDataDestination>()
            SupplierDataScreen(
                accountType = args.accountType,
                onNavigateBack = { navController.popBackStack() },
                onComplete = {
                    navController.navigate(SupplierWelcomeDestination) {
                        popUpTo(OnboardingDestination) { inclusive = true }
                    }
                }
            )
        }


        // ==========================================================
        //  CLIENTE
        // ==========================================================

        composable<LaborDestination> {
            LaborScreen(
                onNavigateToProfile = { navController.navigate(UserProfileDestination) },
                onNavigateToHome = { /* ya estamos en home */ },
                onNavigateToMessages = { navController.navigate(YourRequestsDestination) }
            )
        }

        composable<YourRequestsDestination> {
            YourRequestsScreen() // esta pantalla ya NO recibe callbacks
        }

        composable<UserProfileDestination> {
            UserProfileScreen(
                onNavigateToProfile = { /* ya estamos aquí */ },
                onNavigateToHome = {
                    navController.navigate(LaborDestination) {
                        popUpTo(LaborDestination) { inclusive = true }
                    }
                }
            )
        }


        // ==========================================================
        //  PROVEEDOR
        // ==========================================================

        composable<SupplierWelcomeDestination> {
            SupplierWelcomeScreen(
                onNavigateToProfile = { navController.navigate(SupplierProfileDestination) },
                onNavigateToHome = { /* ya estamos aquí */ }
            )
        }

        composable<SupplierProfileDestination> {
            SupplierProfileScreen(
                onNavigateToProfile = { /* ya estamos aquí */ },
                onNavigateToHome = {
                    navController.navigate(SupplierWelcomeDestination) {
                        popUpTo(SupplierWelcomeDestination) { inclusive = true }
                    }
                }
            )
        }
    }
}
