package uvg.plats.fixerly.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uvg.plats.fixerly.data.local.FixerlyDatabase
import uvg.plats.fixerly.data.local.entities.UserEntity
import java.util.UUID

class TestDatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FixerlyDatabase.getDatabase(application)
    private val userDao = database.userDao()

    private val _testResult = MutableStateFlow<String>("Esperando prueba...")
    val testResult: StateFlow<String> = _testResult

    fun testDatabaseOperations() {
        viewModelScope.launch {
            try {
                // 1. Insertar un usuario de prueba
                val testUser = UserEntity(
                    id = UUID.randomUUID().toString(),
                    nombre = "Juan",
                    apellidos = "Pérez",
                    email = "juan@test.com",
                    telefono = "12345678",
                    accountType = "Cliente",
                    passwordHash = "hashedpassword123"
                )

                userDao.insertUser(testUser)
                _testResult.value = "✅ Usuario insertado correctamente\n"

                // 2. Leer el usuario
                val retrievedUser = userDao.getUserByEmail("juan@test.com")

                if (retrievedUser != null) {
                    _testResult.value += """
                        ✅ Usuario recuperado:
                        - ID: ${retrievedUser.id}
                        - Nombre: ${retrievedUser.nombre} ${retrievedUser.apellidos}
                        - Email: ${retrievedUser.email}
                        - Teléfono: ${retrievedUser.telefono}
                        - Tipo: ${retrievedUser.accountType}
                        
                        🎉 Room está funcionando correctamente!
                    """.trimIndent()
                } else {
                    _testResult.value += "❌ Error: No se pudo recuperar el usuario"
                }

            } catch (e: Exception) {
                _testResult.value = "❌ Error en la base de datos:\n${e.message}"
            }
        }
    }

    fun clearTestData() {
        viewModelScope.launch {
            try {
                val user = userDao.getUserByEmail("juan@test.com")
                if (user != null) {
                    userDao.deleteUser(user)
                    _testResult.value = "🗑️ Datos de prueba eliminados"
                }
            } catch (e: Exception) {
                _testResult.value = "❌ Error al eliminar: ${e.message}"
            }
        }
    }
}
