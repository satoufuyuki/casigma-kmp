package dev.pbt.casigma.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownDbDriver(
    drivers: List<String>,
    currentDriverIndex: Int,
    onDriverSelected: (Int) -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }

    return Box {
        TextButton(
            onClick = { isDropDownExpanded.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                Text(if (currentDriverIndex == -1) "Select Driver" else drivers[currentDriverIndex], style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Localized description")
            }
        }
        DropdownMenu(expanded = isDropDownExpanded.value, onDismissRequest = { isDropDownExpanded.value = false }) {
            drivers.forEachIndexed { index, driver ->
                DropdownMenuItem(
                    text = { Text(
                        driver,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ) },
                    onClick = {
                        isDropDownExpanded.value = false
                        onDriverSelected(index)
                    }
                )
            }
        }
    }

}