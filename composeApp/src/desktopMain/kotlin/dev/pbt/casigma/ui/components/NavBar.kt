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
import dev.pbt.casigma.modules.providers.DialogProvider
import dev.pbt.casigma.modules.utils.AlertUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuBar() {
    val windowScope: FrameWindowScope = koinInject()
    val newTableDialogShown = remember { mutableStateOf(false) }
    val authenticatedUser = koinInject<MutableState<UserObject?>>()
    val dialogProvider: DialogProvider = koinInject()
    var action by remember { mutableStateOf("Last action: None") }
    var isOpen by remember { mutableStateOf(true) }
    var isSubmenuShowing by remember { mutableStateOf(false) }
    val db = koinInject<Database>()
    val navHostController = koinInject<NavHostController>()

    fun handleNewTable(tableNo: Int, customerName: String, additionalNotes: String?) {
        newTableDialogShown.value = false
        transaction(db.conn) {
            val existingTable = Order.select(Order.id).where { (Order.tableNo eq tableNo) and (Order.status neq OrderStatus.Completed) }.count()
            if (existingTable > 0) {
                dialogProvider.setAlertComponent {
                    AlertUtils.buildError(
                        "Table $tableNo is already occupied",
                        onDismiss = {
                            newTableDialogShown.value = true
                        }
                    )
                }.show()
            } else {
                Order.insert {
                    it[Order.tableNo] = tableNo
                    it[Order.customerName] = customerName
                    it[status] = OrderStatus.Pending
                    it[Order.additionalNotes] = additionalNotes ?: "No additional notes"
                }

                dialogProvider.setAlertComponent {
                    AlertUtils.buildSuccess(
                        "Table $tableNo has been successfully added",
                    )
                }.show()
                navHostController.navigate(AppScreen.WaitersRecordOrder.name)
            }
        }
    }

    windowScope.MenuBar {
        if (newTableDialogShown.value) {
            NewTableDialog(onDismiss = {
                newTableDialogShown.value = false
            }, onConfirm = { tableNo, customerName, additionalNotes ->
                if (tableNo == null || customerName == null || tableNo <= 0 || customerName.isEmpty()) {
                    dialogProvider.setAlertComponent {
                        dialogProvider.setAlertComponent { AlertUtils.buildError(
                            "Table number and customer name must be properly provided",
                            onDismiss = {
                                dialogProvider.dismiss()
                                newTableDialogShown.value = true
                            }
                        ) }.show()
                    }.show()
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
                        Item("View Orders", onClick = {
                            navHostController.navigate(AppScreen.WaitersOrderList.name)
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