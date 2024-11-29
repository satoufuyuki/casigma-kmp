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
import androidx.navigation.NavHostController
import dev.pbt.casigma.AppScreen
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.Order
import dev.pbt.casigma.modules.database.models.OrderStatus
import dev.pbt.casigma.modules.database.models.UserObject
import dev.pbt.casigma.modules.database.models.UserRole
import dev.pbt.casigma.modules.providers.Auth
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuBar(windowScope: FrameWindowScope, applicationScope: ApplicationScope) {
    val newTableDialogShown = remember { mutableStateOf(false) }
    val errorDialogShown = remember { mutableStateOf(false) }
    val errorDialogMessage = remember { mutableStateOf("") }
    val successDialogShown = remember { mutableStateOf(false) }
    val successDialogMessage = remember { mutableStateOf("") }

    val authenticatedUser = koinInject<MutableState<UserObject?>>()
    var action by remember { mutableStateOf("Last action: None") }
    var isOpen by remember { mutableStateOf(true) }
    var isSubmenuShowing by remember { mutableStateOf(false) }
    val db = koinInject<Database>()
    var navHostController = koinInject<NavHostController>()

    fun handleNewTable(tableNo: Int, customerName: String, additionalNotes: String?) {
        newTableDialogShown.value = false
        transaction(db.conn) {
            val existingTable = Order.select(Order.id).where { (Order.tableNo eq tableNo) and (Order.status neq OrderStatus.Completed) }.count()
            if (existingTable > 0) {
                errorDialogMessage.value = "Table $tableNo is already occupied"
                errorDialogShown.value = true
            } else {
                Order.insert {
                    it[Order.tableNo] = tableNo
                    it[Order.customerName] = customerName
                    it[status] = OrderStatus.Pending
                    it[Order.additionalNotes] = additionalNotes ?: "No additional notes"
                }

                successDialogMessage.value = "Table $tableNo has been successfully recorded"
                successDialogShown.value = true
                navHostController.navigate(AppScreen.WaitersRecordOrder.name)
            }
        }
    }

    windowScope.MenuBar {
        if (errorDialogShown.value) {
            Alert(
                onDismissRequest = { errorDialogShown.value = false; errorDialogMessage.value = ""; newTableDialogShown.value = true },
                dialogTitle = "Error",
                dialogText = errorDialogMessage.value
            )
        }

        if (successDialogShown.value) {
            Alert(
                onDismissRequest = { successDialogShown.value = false; successDialogMessage.value = "" },
                dialogTitle = "Success",
                dialogText = successDialogMessage.value
            )
        }

        if (newTableDialogShown.value) {
            NewTableDialog(onDismiss = {
                newTableDialogShown.value = false
            }, onConfirm = { tableNo, customerName, additionalNotes ->
                if (tableNo == null || customerName == null || tableNo <= 0 || customerName.isEmpty()) {
                    errorDialogMessage.value = "Table number and customer name must be properly provided"
                    errorDialogShown.value = true
                } else {
                    handleNewTable(tableNo, customerName, additionalNotes)
                }
            })
        }
        if (authenticatedUser.value != null) {
           when {
                authenticatedUser.value!!.role == UserRole.Waiters || authenticatedUser.value!!.role == UserRole.Admin -> {
                    Menu("Orders", mnemonic = 'O') {
                        Item("Record Order", onClick = {
                            newTableDialogShown.value = true
                        })
                    }
                }
           }
        }
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