package dev.pbt.casigma.modules.providers;

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class DialogProvider(
    val isShown: MutableState<Boolean> = mutableStateOf(false),
    private val alertComponent: MutableState<(@Composable () -> Unit)?> = mutableStateOf(null),
) {
    fun setAlertComponent(alertComponent: @Composable () -> Unit): DialogProvider {
        this.alertComponent.value = alertComponent
        return this
    }

    fun show(): DialogProvider {
        isShown.value = true
        return this
    }

    fun dismiss() {
        setAlertComponent {  } // clear previous alert
        isShown.value = false
    }

    @Composable
    fun render() {
        alertComponent.value?.invoke()
    }
}
