package uvg.plats.fixerly.ui.screens.supplier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R

@Composable
fun SupplierProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.supplier_profile_edit_photo),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.supplier_profile_section_info),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileInfoItem(
            label = stringResource(R.string.supplier_profile_profession),
            value = "Electricista"
        )

        ProfileInfoItem(
            label = stringResource(R.string.supplier_profile_experience),
            value = "5 años"
        )

        ProfileInfoItem(
            label = stringResource(R.string.supplier_profile_description),
            value = "Especialista en instalaciones eléctricas"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.supplier_profile_section_services),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.labor_filter_electricity),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Q150.00",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                stringResource(R.string.supplier_profile_save_changes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SupplierProfileScreenPreview() {
    FixerlyTheme {
        SupplierProfileScreen()
    }
}