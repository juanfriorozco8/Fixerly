package uvg.plats.fixerly.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
data object OnboardingDestination

@Serializable
data object LoginDestination

@Serializable
data object RegisterDestination

@Serializable
data object RegisterStepDestination

@Serializable
data object AccountTypeDestination

@Serializable
data object AddressDestination

@Serializable
data class SupplierDataDestination(
    val accountType: String = "Proveedor"
)


@Serializable
data object LaborDestination

@Serializable
data object YourRequestsDestination

@Serializable
data object UserProfileDestination


@Serializable
data object SupplierWelcomeDestination

@Serializable
data object SupplierProfileDestination