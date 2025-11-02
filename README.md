<div align="center">
  <img src="app/src/main/res/drawable/logo_icon.png" alt="Fixerly Logo" width="120"/>
  <h1>Fixerly</h1>
  <p><strong>Let Them Fix It!</strong></p>
</div>

---

## Descripción del Proyecto

**Fixerly** es una app móvil que conecta a personas que necesitan ayuda con reparaciones o mantenimiento (como plomería, electricidad, carpintería, etc.) con proveedores que ofrecen esos servicios.  
La idea es que el usuario pueda publicar su solicitud y recibir respuestas de varios proveedores sin tener que buscarlos uno por uno.

Básicamente: pedís ayuda → te responden los que pueden → elegís a quién contratar.  
Simple, rápido y sin complicarte la vida.

---

## Servicios Externos Implementados

### Firebase Authentication
- Se usa para manejar el **registro e inicio de sesión** de usuarios.
- Diferencia entre cliente y proveedor.
- Permite también la recuperación de contraseñas.
- Plan usado: solo el **Gratuito (Spark Plan)**.

### Cloud Firestore
- Guarda las solicitudes de servicio y las respuestas de los proveedores.
- Permite que todo se actualice **en tiempo real** (si un proveedor responde, el cliente lo ve al instante).
- También se usa para almacenar la información de cada usuario.

### Room Database (SQLite)
- **Base de datos local** que guarda información en el dispositivo.
- Se usa para:
  - Guardar solicitudes en **modo offline** (sin internet).
  - Almacenar el perfil del usuario localmente.
  - Mejorar la velocidad de la app (los datos se cargan más rápido).
- Cuando hay internet, se sincroniza automáticamente con Firestore.
- Plan usado: **Gratis** (viene incluido en Android).
---

## Librerías Usadas

| Librería | Para qué se usa |
|-----------|----------------|
| **Jetpack Compose** | Para diseñar toda la interfaz de la app (pantallas, botones, etc.) |
| **Kotlin Coroutines** | Para manejar tareas en segundo plano sin trabar la app |
| **Navigation Compose** | Para movernos entre pantallas de forma sencilla |
| **Firebase Auth / Firestore** | Para login y base de datos online |
| **Coil Compose** | Para mostrar imágenes (por ejemplo, fotos de perfil) |
| **ViewModel & LiveData** | Para manejar los datos y mantener la interfaz actualizada |

---

## Equipo de Desarrollo

**Fixerly** - Proyecto UVG  
Curso: *Programación de Plataformas Móviles*  
Grupo **10**
