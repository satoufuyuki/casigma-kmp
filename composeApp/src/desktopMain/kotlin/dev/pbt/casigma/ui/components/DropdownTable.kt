package dev.pbt.casigma.ui.components

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
fun DropdownTable(table: List<String>) {
    val isDropDownExpanded = remember { mutableStateOf(false) }

    return Box {
        TextButton(onClick = { isDropDownExpanded.value = true }) {
            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                Text("Table 1", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, fontSize = 25.sp)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Localized description")
            }
        }
        DropdownMenu(expanded = isDropDownExpanded.value, onDismissRequest = { isDropDownExpanded.value = false }) {
            table.forEachIndexed { index, table ->
                DropdownMenuItem(
                    text = { Text(
                        table,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    ) },
                    onClick = {
                        isDropDownExpanded.value = false

                    }
                )
                if (index < table.length - 1) {
                    HorizontalDivider()
                }
            }
        }
    }

}