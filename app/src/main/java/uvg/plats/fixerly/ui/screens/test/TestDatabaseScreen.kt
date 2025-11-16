package uvg.plats.fixerly.ui.screens.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.ui.viewmodel.TestDatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestDatabaseScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: TestDatabaseViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    val testResult by viewModel.testResult.collectAsState()

    FixerlyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Test Room Database",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Prueba de Room Database",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = testResult,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Button(
                    onClick = { viewModel.testDatabaseOperations() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Probar Inserción y Lectura")
                }

                Button(
                    onClick = { viewModel.clearTestData() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Limpiar Datos de Prueba")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        "Volver a Onboarding",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
