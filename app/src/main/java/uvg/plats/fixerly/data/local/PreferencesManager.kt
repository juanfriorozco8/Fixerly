package uvg.plats.fixerly.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fixerly_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = stringPreferencesKey("user_id")
        private val ACCOUNT_TYPE = stringPreferencesKey("account_type")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    suspend fun saveLoginState(userId: String, accountType: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userId
            preferences[ACCOUNT_TYPE] = accountType
            preferences[USER_EMAIL] = email
        }
    }

    suspend fun saveThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        UserPreferences(
            isLoggedIn = preferences[IS_LOGGED_IN] ?: false,
            userId = preferences[USER_ID] ?: "",
            accountType = preferences[ACCOUNT_TYPE] ?: "",
            email = preferences[USER_EMAIL] ?: "",
            themeMode = preferences[THEME_MODE] ?: "system"
        )
    }

    suspend fun clearPreferences() {
        context.dataStore.edit { it.clear() }
    }
}

data class UserPreferences(
    val isLoggedIn: Boolean,
    val userId: String,
    val accountType: String,
    val email: String,
    val themeMode: String
)