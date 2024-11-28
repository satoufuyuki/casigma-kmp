package dev.pbt.casigma.ui.components

import androidx.compose.material.Colors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.pbt.casigma.ui.theme.primaryLight

@Composable
fun Alert(
    onDismissRequest: (() -> Unit)? = null,
    onConfirmation: (() -> Unit)? = null,
    dismissText: @Composable (() -> Unit)? = null,
    confirmText: @Composable (() -> Unit)? = null,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector? = null
) {
    return AlertDialog(
        icon = {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest?.invoke()
        },
        confirmButton = {
            if (confirmText != null) {
                TextButton(
                    onClick = {
                        onConfirmation?.invoke()
                    }
                ) {
                    confirmText()
                }
            }
        },
        dismissButton = {
            if (dismissText != null) {
                TextButton(
                    onClick = {
                        onDismissRequest?.invoke()
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent,
                    )
                ) {
                    dismissText()
                }
            }
        }
    )
}