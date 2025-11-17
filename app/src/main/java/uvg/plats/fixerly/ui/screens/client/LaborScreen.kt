package uvg.plats.fixerly.ui.screens.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.theme.White
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.ui.screens.components.ScreenWithBottomNav
import uvg.plats.fixerly.ui.viewmodel.ServiceViewModel
import uvg.plats.fixerly.ui.viewmodel.OperationState

@Composable
fun LaborScreen(
    clientId: String = "",
    clientName: String = "",
    clientAddress: Address = Address(),
    onNavigateToRequests: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: ServiceViewModel = viewModel()
) {
    var currentRoute by remember { mutableStateOf("home") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var briefDescription by remember { mutableStateOf("") }
    var detailedDescription by remember { mutableStateOf("") }

    val operationState by viewModel.operationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(clientId) {
        if (clientId.isEmpty()) {
            snackbarHostState.showSnackbar("Error: Usuario no identificado")
        }
    }

    LaunchedEffect(operationState) {
        when (val state = operationState) {
            is OperationState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                selectedCategory = null
                briefDescription = ""
                detailedDescription = ""
                viewModel.resetOperationState()
            }
            is OperationState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    FixerlyTheme {
        ScreenWithBottomNav(
            currentRoute = currentRoute,
            onNavigate = { route -> currentRoute = route },
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToHome = onNavigateToHome
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo_icon),
                                    contentDescription = stringResource(R.string.onboarding_logo_description),
                                    modifier = Modifier.size(50.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.app_name),
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White
                                )
                            }

                            Image(
                                painter = painterResource(id = R.drawable.herramientas),
                                contentDescription = stringResource(R.string.labor_image_description),
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.labor_title),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = White,
                            lineHeight = 48.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.labor_category_label),
                            fontSize = 18.sp,
                            color = White,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CategorySelector(
                            selectedCategory = selectedCategory,
                            onCategorySelected = { selectedCategory = it }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = stringResource(R.string.labor_brief_description_label),
                            fontSize = 18.sp,
                            color = White,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(R.string.labor_word_limit),
                            fontSize = 13.sp,
                            color = White.copy(alpha = 0.8f),
                            lineHeight = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = briefDescription,
                            onValueChange = { if (it.split(" ").size <= 10) briefDescription = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.labor_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = White,
                                unfocusedContainerColor = White,
                                focusedIndicatorColor = White,
                                unfocusedIndicatorColor = White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = stringResource(R.string.labor_detailed_description_label),
                            fontSize = 18.sp,
                            color = White,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = detailedDescription,
                            onValueChange = { detailedDescription = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.labor_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = White,
                                unfocusedContainerColor = White,
                                focusedIndicatorColor = White,
                                unfocusedIndicatorColor = White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = {
                                if (selectedCategory != null && clientId.isNotEmpty()) {
                                    viewModel.createServiceRequest(
                                        clientId = clientId,
                                        clientName = clientName,
                                        serviceType = selectedCategory!!,
                                        description = "$briefDescription - $detailedDescription",
                                        address = clientAddress
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            enabled = operationState !is OperationState.Loading &&
                                    selectedCategory != null &&
                                    clientId.isNotEmpty()
                        ) {
                            if (operationState is OperationState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = White
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.labor_button),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySelector(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        stringResource(R.string.labor_filter_plumbing),
        stringResource(R.string.labor_filter_electricity),
        stringResource(R.string.labor_filter_appliances),
        stringResource(R.string.labor_filter_locksmith),
        stringResource(R.string.labor_filter_gardening),
        stringResource(R.string.labor_filter_cleaning),
        stringResource(R.string.labor_filter_painting),
        stringResource(R.string.labor_filter_other)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        categories.chunked(2).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowCategories.forEach { category ->
                    CategoryButton(
                        text = category,
                        isSelected = selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowCategories.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CategoryButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else White,
            contentColor = if (isSelected) White else MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LaborScreenPreview() {
    LaborScreen()
}