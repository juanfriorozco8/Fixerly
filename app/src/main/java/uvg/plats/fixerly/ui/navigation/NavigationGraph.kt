package uvg.plats.fixerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import uvg.plats.fixerly.utils.FirebaseConstants

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

// ViewModel
import uvg.plats.fixerly.ui.viewmodel.AuthViewModel

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
                    if (accountType == FirebaseConstants.TYPE_CLIENT) {
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

        // ============================
        //  REGISTRO (NAV ANIDADO)
        // ============================
        navigation<RegisterDestination>(
            startDestination = RegisterStepDestination
        ) {

            // Paso 1: datos básicos
            composable<RegisterStepDestination> {
                val sharedViewModel: AuthViewModel = viewModel(
                    viewModelStoreOwner = navController.getBackStackEntry(RegisterDestination)
                )

                RegisterScreen(
                    onNavigateToLogin = {
                        navController.navigate(LoginDestination) {
                            popUpTo(RegisterDestination) { inclusive = true }
                        }
                    },
                    onNavigateToAccountType = {
                        navController.navigate(AccountTypeDestination)
                    },
                    viewModel = sharedViewModel
                )
            }

            // Paso 2: tipo de cuenta
            composable<AccountTypeDestination> {
                val sharedViewModel: AuthViewModel = viewModel(
                    viewModelStoreOwner = navController.getBackStackEntry(RegisterDestination)
                )

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

            // Paso 3 (cliente): dirección
            composable<AddressDestination> {
                val sharedViewModel: AuthViewModel = viewModel(
                    viewModelStoreOwner = navController.getBackStackEntry(RegisterDestination)
                )

                AddressScreen(
                    onComplete = {
                        navController.navigate(LaborDestination) {
                            popUpTo(OnboardingDestination) { inclusive = true }
                        }
                    },
                    viewModel = sharedViewModel
                )
            }

            // Paso 3 (proveedor): datos proveedor
            composable<SupplierDataDestination> {
                val sharedViewModel: AuthViewModel = viewModel(
                    viewModelStoreOwner = navController.getBackStackEntry(RegisterDestination)
                )

                SupplierDataScreen(
                    onComplete = {
                        navController.navigate(SupplierWelcomeDestination) {
                            popUpTo(OnboardingDestination) { inclusive = true }
                        }
                    },
                    viewModel = sharedViewModel
                )
            }
        }

        // ============================
        //  CLIENTE
        // ============================

        composable<LaborDestination> {
            LaborScreen()
        }

        composable<YourRequestsDestination> {
            YourRequestsScreen()
        }

        composable<UserProfileDestination> {
            UserProfileScreen()
        }

        // ============================
        //  PROVEEDOR
        // ============================

        composable<SupplierWelcomeDestination> {
            SupplierWelcomeScreen(
                onNavigateToHome = { /* ya estamos en home */ },
                onNavigateToProfile = {
                    navController.navigate(SupplierProfileDestination)
                }
            )
        }

        composable<SupplierProfileDestination> {
            SupplierProfileScreen(
                onNavigateToHome = {
                    navController.navigate(SupplierWelcomeDestination) {
                        popUpTo(SupplierWelcomeDestination) { inclusive = true }
                    }
                },
                onNavigateToProfile = { /* ya estamos en perfil */ }
            )
        }
    }
}
