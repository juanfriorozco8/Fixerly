package uvg.plats.fixerly.ui.theme

import androidx.compose.ui.graphics.Color

// ========== LIGHT MODE COLORS ==========
val FixerlyBlue = Color(0xFF4A90A4)
val FixerlyBlueDark = Color(0xFF1B4F72)
val FixerlyGreen = Color(0xFF52B788)

val TextFieldBackground = Color(0xFFAED6F1)
val TextFieldPlaceholder = Color(0xFF5DADE2)
val TextFieldText = Color(0xFF1B4F72)

// ========== DARK MODE COLORS ==========
// CAMBIO 1: Degradado más sutil (menos contraste)
val DarkBackgroundTop = Color(0xFF0B1419)        // ← CAMBIO: Era 0xFF05090F, ahora un poco más claro
val DarkBackgroundBottom = Color(0xFF1A2F42)     // ← CAMBIO: Era 0xFF0D1922, ahora más claro

val DarkSurface = Color(0xFF1A2F42)
val DarkStatusBar = Color(0xFF0B1419)            // ← CAMBIO: Match con nuevo DarkBackgroundTop
val DarkPrimary = Color(0xFF3B7EA1)

// CAMBIO 2: Verde más claro pero que haga match con dark mode
val DarkSecondary = Color(0xFF5CB88A)            // ← CAMBIO: Era 0xFF4A9B6E, ahora más claro
val DarkSecondaryDark = Color(0xFF3D8B64)        // ← CAMBIO: Era 0xFF2D5F47, ahora más claro

// ========== UNIVERSAL ==========
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)