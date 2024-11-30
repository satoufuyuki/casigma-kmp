package dev.pbt.casigma.modules.utils

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.pbt.casigma.modules.providers.DialogProvider
import dev.pbt.casigma.ui.components.AlertDialog
import org.koin.compose.koinInject

object AlertUtils {
    @Composable
    fun buildError(
        message: String,
        dialogTitle: String = "Error",
        onDismiss: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        confirmText: (@Composable () -> Unit)? = { Text("OK", style = MaterialTheme.typography.bodyMedium, color = Color.White) },
        dismissText: (@Composable () -> Unit)? = null,
    ) {
        val dialogProvider: DialogProvider = koinInject()
        AlertDialog(
            onDismissRequest = { dialogProvider.dismiss(); onDismiss?.invoke() },
            onConfirmation = { dialogProvider.dismiss(); onConfirm?.invoke() },
            dismissText = dismissText,
            confirmText = confirmText,
            dialogTitle = dialogTitle,
            dialogText = message,
        )
    }

    @Composable
    fun buildSuccess(
        message: String,
        dialogTitle: String = "Success",
        onDismiss: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        confirmText: (@Composable () -> Unit)? = { Text("OK", style = MaterialTheme.typography.bodyMedium, color = Color.White) },
        dismissText: (@Composable () -> Unit)? = null,
    ) {
        val dialogProvider: DialogProvider = koinInject()
        AlertDialog(
            onDismissRequest = { dialogProvider.dismiss(); onDismiss?.invoke() },
            onConfirmation = { dialogProvider.dismiss(); onConfirm?.invoke() },
            dismissText = dismissText,
            confirmText = confirmText,
            dialogTitle = dialogTitle,
            dialogText = message,
        )
    }

    @Composable
    fun buildPromptDecision(
        message: String,
        dialogTitle: String = "Alert",
        onDismiss: (() -> Unit)? = null,
        onConfirm: (() -> Unit)? = null,
        confirmText: (@Composable () -> Unit)? = { Text("Confirm", style = MaterialTheme.typography.bodyMedium, color = Color.White) },
        dismissText: (@Composable () -> Unit)? = { Text("Cancel", style = MaterialTheme.typography.bodyMedium, color = Color.White) },
    ) {
        val dialogProvider: DialogProvider = koinInject()
        AlertDialog(
            onDismissRequest = { dialogProvider.dismiss(); onDismiss?.invoke() },
            onConfirmation = { dialogProvider.dismiss(); onConfirm?.invoke() },
            dismissText = dismissText,
            confirmText = confirmText,
            dialogTitle = dialogTitle,
            dialogText = message,
        )
    }
}