package uvg.plats.fixerly.ui.screens.supplier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R

@Composable
fun SupplierWelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .padding(vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.supplier_welcome_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.supplier_welcome_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatsCard(
                label = stringResource(R.string.supplier_welcome_stats_requests),
                value = "5"
            )
            StatsCard(
                label = stringResource(R.string.supplier_welcome_stats_completed),
                value = "12"
            )
            StatsCard(
                label = stringResource(R.string.supplier_welcome_stats_rating),
                value = "4.5"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.supplier_welcome_section_requests),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.labor_filter_plumbing),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.supplier_welcome_request_accept),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SupplierWelcomeScreenPreview() {
    FixerlyTheme {
        SupplierWelcomeScreen()
    }
}