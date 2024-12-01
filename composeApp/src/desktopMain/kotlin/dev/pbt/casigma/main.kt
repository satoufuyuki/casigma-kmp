package dev.pbt.casigma

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScaledApp(scaleFactor: Float, content: @Composable (Float) -> Unit) {
    CompositionLocalProvider(LocalDensity provides Density(density = scaleFactor)) {
        content(scaleFactor)
    }
}

fun main() = application {
    var scaleFactor by remember { mutableStateOf(1.0f) }

    System.setProperty("sun.java2d.dpiaware", "true")
    Window(
        onCloseRequest = ::exitApplication,
        title = "casigma",
        state = WindowState(placement = WindowPlacement.Maximized),
        resizable = false,
//        undecorated = true
        onKeyEvent = { event ->
            // Global key event handling
            if (event.type == KeyEventType.KeyDown && event.isCtrlPressed) {
                when (event.key) {
                    Key.Plus, Key.Equals -> {
                        scaleFactor = (scaleFactor + 0.1f).coerceIn(0.5f, 3.0f)
                        true
                    }
                    Key.Minus -> {
                        scaleFactor = (scaleFactor - 0.1f).coerceIn(0.5f, 3.0f)
                        true
                    }
                    Key.Zero -> {
                        scaleFactor = 1.0f // Reset scale
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
    ) {
        ScaledApp(scaleFactor) {
            App(this)
        }
    }
}