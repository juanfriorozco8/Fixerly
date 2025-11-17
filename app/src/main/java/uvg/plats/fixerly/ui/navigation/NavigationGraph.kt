package uvg.plats.fixerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.utils.FirebaseConstants
import uvg.plats.fixerly.ui.screens.auth.OnboardingScreen
import uvg.plats.fixerly.ui.screens.auth.LoginScreen
import uvg.plats.fixerly.ui.screens.auth.RegisterScreen
import uvg.plats.fixerly.ui.screens.auth.AccountTypeScreen
import uvg.plats.fixerly.ui.screens.auth.AddressScreen
import uvg.plats.fixerly.ui.screens.auth.SupplierDataScreen
import uvg.plats.fixerly.ui.screens.client.LaborScreen
import uvg.plats.fixerly.ui.screens.client.YourRequestsScreen
import uvg.plats.fixerly.ui.screens.client.UserProfileScreen
import uvg.plats.fixerly.ui.screens.supplier.SupplierWelcomeScreen
import uvg.plats.fixerly.ui.screens.supplier.SupplierProfileScreen
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

        composable<LaborDestination> {
            val authViewModel: AuthViewModel = viewModel()
            val currentUser by authViewModel.currentUser.collectAsState()

            LaborScreen(
                clientId = currentUser?.userId ?: "",
                clientName = currentUser?.getFullName() ?: "",
                clientAddress = currentUser?.address ?: Address(),
                onNavigateToRequests = {
                    navController.navigate(YourRequestsDestination)
                },
                onNavigateToProfile = {
                    navController.navigate(UserProfileDestination)
                },
                onNavigateToHome = {}
            )
        }

        composable<YourRequestsDestination> {
            val authViewModel: AuthViewModel = viewModel()
            val currentUser by authViewModel.currentUser.collectAsState()

            YourRequestsScreen(
                clientId = currentUser?.userId ?: "",
                onNavigateToProfile = {
                    navController.navigate(UserProfileDestination)
                },
                onNavigateToHome = {
                    navController.navigate(LaborDestination)
                }
            )
        }

        composable<UserProfileDestination> {
            val authViewModel: AuthViewModel = viewModel()
            val currentUser by authViewModel.currentUser.collectAsState()

            UserProfileScreen(
                userId = currentUser?.userId ?: "",
                onNavigateToProfile = {},
                onNavigateToHome = {
                    navController.navigate(LaborDestination)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(OnboardingDestination) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<SupplierWelcomeDestination> {
            SupplierWelcomeScreen(
                onNavigateToHome = {},
                onNavigateToProfile = {
                    navController.navigate(SupplierProfileDestination)
                }
            )
        }

        composable<SupplierProfileDestination> {
            val authViewModel: AuthViewModel = viewModel()

            SupplierProfileScreen(
                onNavigateToHome = {
                    navController.navigate(SupplierWelcomeDestination)
                },
                onNavigateToProfile = {},
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(OnboardingDestination) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}