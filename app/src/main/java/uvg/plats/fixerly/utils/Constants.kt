package uvg.plats.fixerly.utils

object FirebaseConstants {


    const val USERS_COLLECTION = "users"
    const val SERVICE_REQUESTS_COLLECTION = "serviceRequests"
    const val RATINGS_COLLECTION = "ratings"

    const val FIELD_USER_ID = "userId"
    const val FIELD_NAME = "name"
    const val FIELD_LAST_NAME = "lastName"
    const val FIELD_EMAIL = "email"
    const val FIELD_PHONE = "phone"
    const val FIELD_USER_TYPE = "userType"
    const val FIELD_CREATED_AT = "createdAt"
    const val FIELD_PROFILE_IMAGE_URL = "profileImageUrl"

    const val FIELD_RATING = "rating"
    const val FIELD_RATING_COUNT = "ratingCount"

    const val TYPE_CLIENT = "client"
    const val TYPE_PROVIDER = "provider"


    const val FIELD_ADDRESS = "address"
    const val FIELD_DEPARTMENT = "department"
    const val FIELD_ZONE = "zone"
    const val FIELD_DIRECTIONS = "directions"

    const val FIELD_CONTACT_PREFERENCES = "contactPreferences"
    const val FIELD_ABOUT = "about"
    const val FIELD_SKILLS = "skills"

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

    const val FIELD_CLIENT_ID = "clientId"
    const val FIELD_CLIENT_NAME = "clientName"
    const val FIELD_SERVICE_TYPE = "serviceType"
    const val FIELD_DESCRIPTION = "description"
    const val FIELD_STATUS = "status"
    const val FIELD_RESPONSES = "responses"
    const val FIELD_ACCEPTED_PROVIDER_ID = "acceptedProviderId"

    object RequestStatus {
        const val PENDING = "pending"
        const val IN_PROGRESS = "in_progress"
        const val COMPLETED = "completed"
        const val CANCELLED = "cancelled"
    }

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


    const val STORAGE_PROFILE_IMAGES = "profile_images"
}
