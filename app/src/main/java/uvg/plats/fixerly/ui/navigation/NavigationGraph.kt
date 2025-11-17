package uvg.plats.fixerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

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

        composable<OnboardingDestination> {
            OnboardingScreen(
                onNavigateToLogin = { navController.navigate(LoginDestination) },
                onNavigateToRegister = { navController.navigate(RegisterDestination) }
            )
        }

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

        // viewModel compartido
        navigation<RegisterDestination>(
            startDestination = RegisterStepDestination
        ) {

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

        // cliente

        composable<LaborDestination> {
            LaborScreen()
        }

        composable<YourRequestsDestination> {
            YourRequestsScreen()
        }

        composable<UserProfileDestination> {
            UserProfileScreen()
        }

        // proovedor

        composable<SupplierWelcomeDestination> {
            SupplierWelcomeScreen()
        }

        composable<SupplierProfileDestination> {
            SupplierProfileScreen()
        }
    }
}