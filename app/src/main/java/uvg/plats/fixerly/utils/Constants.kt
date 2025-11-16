package uvg.plats.fixerly.utils

// Constantes de Firebase
object FirebaseConstants {
    // Colecciones de Firestore
    const val USERS_COLLECTION = "users"
    const val SERVICE_REQUESTS_COLLECTION = "service_requests"
    const val PROVIDERS_COLLECTION = "providers"

    // Campos de usuario
    const val FIELD_USER_ID = "userId"
    const val FIELD_NAME = "name"
    const val FIELD_LAST_NAME = "lastName"
    const val FIELD_EMAIL = "email"
    const val FIELD_PHONE = "phone"
    const val FIELD_USER_TYPE = "userType"
    const val FIELD_CREATED_AT = "createdAt"
    const val FIELD_PROFILE_IMAGE_URL = "profileImageUrl"

    // Tipos de usuario
    const val USER_TYPE_CLIENT = "client"
    const val USER_TYPE_PROVIDER = "provider"

    // Campos de dirección (para clientes)
    const val FIELD_ADDRESS = "address"
    const val FIELD_DEPARTMENT = "department"
    const val FIELD_ZONE = "zone"
    const val FIELD_DIRECTIONS = "directions"

    // Campos de proveedor
    const val FIELD_CONTACT_PREFERENCES = "contactPreferences"
    const val FIELD_ABOUT = "about"
    const val FIELD_SKILLS = "skills"

    // Habilidades disponibles
    object Skills {
        const val PLUMBING = "plomeria"
        const val ELECTRICITY = "electricidad"
        const val APPLIANCES = "electrodomesticos"
        const val LOCKSMITH = "cerrajeria"
        const val GARDENING = "jardines_exteriores"
        const val CLEANING = "limpieza_lavanderia"
        const val PAINTING = "pintura_acabados"
        const val OTHER = "otros"
    }

    // Estados de solicitud
    const val REQUEST_STATUS_PENDING = "pending"
    const val REQUEST_STATUS_ACCEPTED = "accepted"
    const val REQUEST_STATUS_REJECTED = "rejected"
    const val REQUEST_STATUS_COMPLETED = "completed"

    // Campos de solicitud de servicio
    const val FIELD_CLIENT_ID = "clientId"
    const val FIELD_SERVICE_TYPE = "serviceType"
    const val FIELD_DESCRIPTION = "description"
    const val FIELD_STATUS = "status"
    const val FIELD_RESPONSES = "responses"

    // Storage paths
    const val STORAGE_PROFILE_IMAGES = "profile_images"
}

// Mensajes de error comunes
object ErrorMessages {
    const val NETWORK_ERROR = "Error de conexión. Verifica tu internet."
    const val AUTH_FAILED = "Error de autenticación. Verifica tus credenciales."
    const val USER_NOT_FOUND = "Usuario no encontrado."
    const val EMAIL_ALREADY_IN_USE = "Este correo ya está registrado."
    const val WEAK_PASSWORD = "La contraseña debe tener al menos 6 caracteres."
    const val INVALID_EMAIL = "Correo electrónico inválido."
    const val GENERIC_ERROR = "Ocurrió un error. Intenta de nuevo."
}