package dev.pbt.casigma.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.pbt.casigma.ui.theme.primaryLight
import dev.pbt.casigma.ui.theme.white

@Composable
fun NewTableDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int?, String?, String?) -> Unit
) {
    val tableNumber = remember {
        mutableStateOf<Int?>(null)
    }

    val customerName = remember {
        mutableStateOf<String?>(null)
    }

    val additionalNotes = remember {
        mutableStateOf<String?>(null)
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                Text(
                    text = "Add New Table",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
                ) {
                    OutlinedTextField(
                        value = tableNumber.value?.toString() ?: "",
                        onValueChange = {
                            tableNumber.value = it.toIntOrNull()
                        },
                        label = {
                            Text("Table Number")
                        }
                    )
                    OutlinedTextField(
                        value = customerName.value ?: "",
                        onValueChange = {
                            customerName.value = it
                        },
                        label = {
                            Text("Customer Name")
                        }
                    )
                    OutlinedTextField(
                        value = additionalNotes.value ?: "",
                        onValueChange = {
                            additionalNotes.value = it
                        },
                        label = {
                            Text("Additional Notes")
                        }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text("Cancel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = primaryLight,
                        )
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = primaryLight,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            onConfirm(tableNumber.value, customerName.value, additionalNotes.value)
                        },
                    ) {
                        Text("Submit",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = white,
                        )
                    }
                }
            }
        }
    }
}