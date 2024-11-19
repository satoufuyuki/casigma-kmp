package dev.pbt.casigma.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuBar(windowScope: FrameWindowScope, applicationScope: ApplicationScope) {
    var action by remember { mutableStateOf("Last action: None") }
    var isOpen by remember { mutableStateOf(true) }
    var isSubmenuShowing by remember { mutableStateOf(false) }
    windowScope.MenuBar {
        Menu("File", mnemonic = 'F') {
            Item("Copy", onClick = {}, shortcut = KeyShortcut(Key.C, ctrl = true))
            Item("Paste", onClick = {}, shortcut = KeyShortcut(Key.V, ctrl = true))
        }
        Menu("Actions", mnemonic = 'A') {
            CheckboxItem(
                "Advanced settings",
                checked = isSubmenuShowing,
                onCheckedChange = {
                    isSubmenuShowing = !isSubmenuShowing
                }
            )
            if (isSubmenuShowing) {
                Menu("Settings") {
                    Item("Setting 1", onClick = { action = "Last action: Setting 1" })
                    Item("Setting 2", onClick = { action = "Last action: Setting 2" })
                }
            }
            Separator()
            Item("About", icon = AboutIcon, onClick = { action = "Last action: About" })
            Item("Exit", onClick = { isOpen = false }, shortcut = KeyShortcut(Key.Escape), mnemonic = 'E')
        }
    }

}

object AboutIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}