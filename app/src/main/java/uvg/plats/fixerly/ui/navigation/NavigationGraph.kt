package uvg.plats.fixerly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import uvg.plats.fixerly.ui.viewmodel.AuthState

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = viewModel()

    LaunchedEffect(Unit) {
        authViewModel.checkUserLoggedIn()
    }

    NavHost(
        navController = navController,
        startDestination = OnboardingDestination
    ) {

        composable<OnboardingDestination> {
            val currentUser by authViewModel.currentUser.collectAsState()
            val authState by authViewModel.authState.collectAsState()

            LaunchedEffect(authState, currentUser) {
                if (authState is AuthState.Success && currentUser != null) {
                    val destination = if (currentUser!!.userType == FirebaseConstants.TYPE_CLIENT) {
                        YourRequestsDestination
                    } else {
                        SupplierWelcomeDestination
                    }
                    navController.navigate(destination) {
                        popUpTo(OnboardingDestination) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginDestination) {
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterDestination) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<LoginDestination> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(RegisterDestination) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = { accountType ->
                    val destination = if (accountType == FirebaseConstants.TYPE_CLIENT) {
                        YourRequestsDestination
                    } else {
                        SupplierWelcomeDestination
                    }
                    navController.navigate(destination) {
                        popUpTo(OnboardingDestination) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                viewModel = authViewModel
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
                            launchSingleTop = true
                        }
                    },
                    onNavigateToAccountType = {
                        navController.navigate(AccountTypeDestination) {
                            launchSingleTop = true
                        }
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
                        navController.navigate(LoginDestination) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToNext = { accountType ->
                        if (accountType == "Proveedor") {
                            navController.navigate(SupplierDataDestination(accountType)) {
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(AddressDestination) {
                                launchSingleTop = true
                            }
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
                        navController.navigate(YourRequestsDestination) {
                            popUpTo(OnboardingDestination) { inclusive = true }
                            launchSingleTop = true
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
                            launchSingleTop = true
                        }
                    },
                    viewModel = sharedViewModel
                )
            }
        }

        composable<LaborDestination> {
            val currentUser by authViewModel.currentUser.collectAsState()

            LaborScreen(
                clientId = currentUser?.userId ?: "",
                clientName = currentUser?.getFullName() ?: "",
                clientAddress = currentUser?.address ?: Address(),
                onNavigateToRequests = {
                    navController.navigate(YourRequestsDestination) {
                        popUpTo(YourRequestsDestination) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(UserProfileDestination) {
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                }
            )
        }

        composable<YourRequestsDestination> {
            val currentUser by authViewModel.currentUser.collectAsState()

            YourRequestsScreen(
                clientId = currentUser?.userId ?: "",
                onNavigateToProfile = {
                    navController.navigate(UserProfileDestination) {
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                },
                onNavigateToNewRequest = {
                    navController.navigate(LaborDestination) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<UserProfileDestination> {
            val currentUser by authViewModel.currentUser.collectAsState()

            UserProfileScreen(
                userId = currentUser?.userId ?: "",
                onNavigateToProfile = {
                },
                onNavigateToHome = {
                    navController.navigate(YourRequestsDestination) {
                        popUpTo(YourRequestsDestination) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToNewRequest = {
                    navController.navigate(LaborDestination) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(OnboardingDestination) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<SupplierWelcomeDestination> {
            val currentUser by authViewModel.currentUser.collectAsState()

            SupplierWelcomeScreen(
                currentUser = currentUser,
                onNavigateToHome = {
                },
                onNavigateToProfile = {
                    navController.navigate(SupplierProfileDestination) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<SupplierProfileDestination> {
            SupplierProfileScreen(
                onNavigateToHome = {
                    navController.navigate(SupplierWelcomeDestination) {
                        popUpTo(SupplierWelcomeDestination) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(OnboardingDestination) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}