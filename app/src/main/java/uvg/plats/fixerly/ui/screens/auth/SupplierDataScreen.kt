package uvg.plats.fixerly.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R

@Composable
fun SupplierDataScreen(
    accountType: String = "Proveedor",  // Recibe tipo de cuenta desde navegación
    onNavigateBack: () -> Unit = {},    // Callback para regresar
    onComplete: () -> Unit = {}         // Callback cuando complete el registro
) {
    var contactoEmail by remember { mutableStateOf(false) }
    var contactoTelefono by remember { mutableStateOf(false) }

    val habilidades = listOf(
        "Plomería", "Electricidad", "Electrodomésticos",
        "Cerrajería", "Jardinería y exteriores",
        "Limpieza y lavandería", "Pintura y acabados", "Otros..."
    )

    val habilidadesSeleccionadas = remember { mutableStateMapOf<String, Boolean>() }
    habilidades.forEach { habilidad ->
        if (habilidadesSeleccionadas[habilidad] == null)
            habilidadesSeleccionadas[habilidad] = false
    }

    var detalles by remember { mutableStateOf(TextFieldValue("")) }

    FixerlyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())  // Agrega scroll
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_icon),
                        contentDescription = "Logo",
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = "Fixerly.",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿Listo para comenzar?",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Cuéntanos más sobre ti",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Esta será la información que recibirá el usuario para contratar un servicio!",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Forma de Contacto:",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = contactoEmail,
                                onCheckedChange = { contactoEmail = it },
                                colors = CheckboxDefaults.colors(checkmarkColor = MaterialTheme.colorScheme.onPrimary)
                            )
                            Text("Correo electrónico", color = MaterialTheme.colorScheme.onPrimary)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = contactoTelefono,
                                onCheckedChange = { contactoTelefono = it },
                                colors = CheckboxDefaults.colors(checkmarkColor = MaterialTheme.colorScheme.onPrimary)
                            )
                            Text("Teléfono", color = MaterialTheme.colorScheme.onPrimary)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "¡Agrega más detalles sobre ti! (Opcional)",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = detalles,
                            onValueChange = { detalles = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            placeholder = { Text("Escribe aquí...") },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                cursorColor = MaterialTheme.colorScheme.onPrimary,
                                focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Selecciona tus habilidades:",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        habilidades.forEach { habilidad ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = habilidadesSeleccionadas[habilidad] == true,
                                    onCheckedChange = { habilidadesSeleccionadas[habilidad] = it },
                                    colors = CheckboxDefaults.colors(checkmarkColor = MaterialTheme.colorScheme.onPrimary)
                                )
                                Text(habilidad, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        // Guarda datos de proveedor y completar registro
                                // Guardar habilidades y detalles en base de datos
                        onComplete()  // Usa callback para navegar a pantalla principal
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("¡Listo!", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSecondary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Olvidaste algo? ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Regresar",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onNavigateBack() }  // Usa callback
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SupplierDataScreenPreview() {
    SupplierDataScreen()
}
