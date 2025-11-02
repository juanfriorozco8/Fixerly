package uvg.plats.fixerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
// auth
import uvg.plats.fixerly.ui.screens.auth.OnboardingScreen
import uvg.plats.fixerly.ui.screens.auth.LoginScreen
import uvg.plats.fixerly.ui.screens.auth.RegisterScreen
import uvg.plats.fixerly.ui.screens.auth.AccountTypeScreen
import uvg.plats.fixerly.ui.screens.auth.AddressScreen
import uvg.plats.fixerly.ui.screens.auth.SupplierDataScreen
//client
import uvg.plats.fixerly.ui.screens.client.LaborScreen
import uvg.plats.fixerly.ui.screens.client.TusSolicitudesScreen
import uvg.plats.fixerly.ui.screens.client.UserProfileScreen
//supplier
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
        // parte del login y registro, como lo principal
        composable<OnboardingDestination> {
            OnboardingScreen(
                onNavigateToLogin = { navController.navigate(LoginDestination) },
                onNavigateToRegister = { navController.navigate(RegisterDestination) }
            )
        }

        // Login
        composable<LoginDestination> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(RegisterDestination) {
                        popUpTo(OnboardingDestination) { inclusive = false }
                    }
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

        // registro
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

        // tipo de cuenta
        composable<AccountTypeDestination> {
            AccountTypeScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginDestination) {
                        popUpTo(OnboardingDestination) { inclusive = false }
                    }
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

        // localizacion del cliente
        composable<AddressDestination> {
            AddressScreen(
                onComplete = {
                    navController.navigate(LaborDestination) {
                        popUpTo(OnboardingDestination) { inclusive = true }
                    }
                }
            )
        }

        // data del proveedor
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

        // pantallas del cliente
        composable<LaborDestination> {
            LaborScreen(
                onNavigateToProfile = { navController.navigate(UserProfileDestination) },
                onNavigateToHome = { /* Ya estamos aquí */ },
                onNavigateToMessages = { navController.navigate(YourRequestsDestination) }
            )
        }

        composable<YourRequestsDestination> {
            TusSolicitudesScreen(
                onNavigateToProfile = { navController.navigate(UserProfileDestination) },
                onNavigateToHome = {
                    navController.navigate(LaborDestination) {
                        popUpTo(LaborDestination) { inclusive = true }
                    }
                },
                onNavigateToMessages = { /* Ya estamos aquí */ }
            )
        }

        composable<UserProfileDestination> {
            UserProfileScreen(
                onNavigateToProfile = { /* Ya estamos aquí */ },
                onNavigateToHome = {
                    navController.navigate(LaborDestination) {
                        popUpTo(LaborDestination) { inclusive = true }
                    }
                },
                //onNavigateToMessages = { navController.navigate(YourRequestsDestination) }
            )
        }

        // === SUPPLIER SCREENS ===
        composable<SupplierWelcomeDestination> {
            SupplierWelcomeScreen(
                onNavigateToProfile = { navController.navigate(SupplierProfileDestination) },
                onNavigateToHome = { /* Ya estamos aquí */ },
                //onNavigateToMessages = { /* Por implementar */ }
            )
        }

        composable<SupplierProfileDestination> {
            SupplierProfileScreen(
                onNavigateToProfile = { /* Ya estamos aquí */ },
                onNavigateToHome = {
                    navController.navigate(SupplierWelcomeDestination) {
                        popUpTo(SupplierWelcomeDestination) { inclusive = true }
                    }
                },
                //onNavigateToMessages = { /* Por implementar */ }
            )
        }
    }
}