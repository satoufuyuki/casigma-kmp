package dev.pbt.casigma

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import dev.pbt.casigma.ui.components.MenuBar


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "casigma",
        state = WindowState(placement = WindowPlacement.Maximized),
        resizable = false,
//        undecorated = true
    ) {
        MenuBar(this, this@application)
        App()
    }
}