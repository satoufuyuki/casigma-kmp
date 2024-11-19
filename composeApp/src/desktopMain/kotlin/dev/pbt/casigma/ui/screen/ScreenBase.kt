package dev.pbt.casigma.ui.screen

import androidx.compose.runtime.Composable

open class ScreenBase(open val route: String) {
    @Composable
    open fun render(): Unit {
        throw NotImplementedError("An operation is not implemented: Not yet implemented")
    }
}