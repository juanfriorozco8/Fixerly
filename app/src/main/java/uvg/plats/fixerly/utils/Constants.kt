package uvg.plats.fixerly.utils

object FirebaseConstants {

    // -------------------------
    // Firestore Collections
    // -------------------------
    const val USERS = "users"
    const val SERVICE_REQUESTS = "serviceRequests"

    // -------------------------
    // User fields
    // -------------------------
    const val USER_ID = "userId"
    const val NAME = "name"
    const val LAST_NAME = "lastName"
    const val EMAIL = "email"
    const val PHONE = "phone"
    const val USER_TYPE = "userType"
    const val CREATED_AT = "createdAt"
    const val PROFILE_IMAGE_URL = "profileImageUrl"

    // Ratings
    const val RATING = "rating"
    const val RATING_COUNT = "ratingCount"

    // User types
    const val TYPE_CLIENT = "client"
    const val TYPE_PROVIDER = "provider"

    // -------------------------
    // Address fields
    // -------------------------
    const val ADDRESS = "address"
    const val DEPARTMENT = "department"
    const val ZONE = "zone"
    const val DIRECTIONS = "directions"

    // -------------------------
    // Provider-specific fields
    // -------------------------
    const val CONTACT_PREFERENCES = "contactPreferences"
    const val ABOUT = "about"
    const val SKILLS = "skills"

    object Skills {
        const val PLUMBING = "plomeria"
        const val ELECTRICITY = "electricidad"
        const val APPLIANCES = "electrodomesticos"
        const val LOCKSMITH = "cerrajeria"
        const val GARDENING = "jardineria"
        const val CLEANING = "limpieza"
        const val PAINTING = "pintura"
        const val OTHER = "otros"
    }

    // -------------------------
    // ServiceRequest fields
    // -------------------------
    const val CLIENT_ID = "clientId"
    const val CLIENT_NAME = "clientName"
    const val SERVICE_TYPE = "serviceType"
    const val DESCRIPTION = "description"
    const val STATUS = "status"
    const val RESPONSES = "responses"
    const val ACCEPTED_PROVIDER_ID = "acceptedProviderId"

    // ServiceRequest statuses
    object RequestStatus {
        const val PENDING = "pending"
        const val IN_PROGRESS = "in_progress"
        const val COMPLETED = "completed"
        const val CANCELLED = "cancelled"
    }

    // -------------------------
    // ProviderResponse fields
    // -------------------------
    object ProviderResponseFields {
        const val PROVIDER_ID = "providerId"
        const val PROVIDER_NAME = "providerName"
        const val PROVIDER_EMAIL = "providerEmail"
        const val PROVIDER_PHONE = "providerPhone"
        const val MESSAGE = "message"
        const val CONTACT_PREFERENCES = "contactPreferences"
        const val STATUS = "status"
        const val SKILLS = "skills"
        const val RESPONDED_AT = "respondedAt"
    }

    object ProviderResponseStatus {
        const val PENDING = "pending"
        const val ACCEPTED = "accepted"
        const val REJECTED = "rejected"
    }

    // -------------------------
    // Storage paths
    // -------------------------
    const val STORAGE_PROFILE_IMAGES = "profile_images"
}
