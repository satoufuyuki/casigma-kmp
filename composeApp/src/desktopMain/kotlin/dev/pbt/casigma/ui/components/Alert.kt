package dev.pbt.casigma.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.pbt.casigma.ui.theme.primaryLight

@Composable
fun AlertDialog(
    onDismissRequest: (() -> Unit)? = null,
    onConfirmation: (() -> Unit)? = null,
    dismissText: @Composable (() -> Unit)? = null,
    confirmText: @Composable (() -> Unit)? = null,
    dialogTitle: String,
    dialogText: String,
) {
    return Dialog(
        onDismissRequest = onDismissRequest ?: {},
    ) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            ) {
                Text(
                    text = dialogTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = dialogText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
                ) {
                    if (dismissText != null) {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors()
                                .copy(
                                    contentColor = Color.Gray
                                ),
                            onClick = {
                                onDismissRequest?.invoke()
                            }
                        ) {
                            dismissText()
                        }
                    }
                    if (confirmText != null) {
                        Button(
                            colors = ButtonDefaults.buttonColors()
                                .copy(
                                    contentColor = Color.White,
                                    containerColor = primaryLight
                                ),
                            onClick = {
                                onConfirmation?.invoke()
                            }
                        ) {
                            confirmText()
                        }
                    }
                }
            }
        }
    }
}