package uvg.plats.fixerly.ui.navigation

import kotlinx.serialization.Serializable


// === PRUEBA DE DATABASE (TEMPORAL) ===
//@Serializable
//data object TestDatabaseDestination


// flujo de la autenticacion, login y eso
@Serializable
data object OnboardingDestination

@Serializable
data object LoginDestination

@Serializable
data object RegisterDestination

@Serializable
data object AccountTypeDestination

@Serializable
data object AddressDestination

@Serializable
data class SupplierDataDestination(
    val accountType: String = "Proveedor"
)


// navs y flujo del cliente
@Serializable
data object LaborDestination

@Serializable
data object YourRequestsDestination

@Serializable
data object UserProfileDestination


// navs y flujo del proveedor
@Serializable
data object SupplierWelcomeDestination

@Serializable
data object SupplierProfileDestination