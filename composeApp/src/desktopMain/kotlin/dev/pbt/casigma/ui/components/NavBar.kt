package dev.pbt.casigma.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import androidx.navigation.NavHostController
import dev.pbt.casigma.AppScreen
import dev.pbt.casigma.GlobalContext
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.Order
import dev.pbt.casigma.modules.database.models.OrderStatus
import dev.pbt.casigma.modules.database.models.UserObject
import dev.pbt.casigma.modules.database.models.UserRole
import dev.pbt.casigma.modules.datastore.SettingRepository
import dev.pbt.casigma.modules.providers.DialogProvider
import dev.pbt.casigma.modules.utils.AlertUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import kotlin.system.exitProcess

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuBar() {
    val globalDatabaseConnected = koinInject<MutableState<Boolean>>(qualifier = named(GlobalContext.DatabaseConnected))
    val globalLastDatabaseError = koinInject<MutableState<String?>>(qualifier = named(GlobalContext.LastDatabaseError))
    val settingRepository: SettingRepository = koinInject()

    val windowScope: FrameWindowScope = koinInject()
    val authenticatedUser = koinInject<MutableState<UserObject?>>()
    val dialogProvider: DialogProvider = koinInject()
    val db = koinInject<Database>()
    val navHostController = koinInject<NavHostController>()

    fun handleNewTable(tableNo: Int?, customerName: String?, additionalNotes: String?) {
        if (tableNo == null || customerName == null || tableNo <= 0 || customerName.isEmpty()) {
            dialogProvider.setAlertComponent {
                dialogProvider.setAlertComponent {
                    AlertUtils.buildError(
                        "Table number and customer name must be properly provided",
                    )
                }.show()
            }.show()
        } else {
            transaction(db.connect()) {
                val existingTable = Order.select(Order.id)
                    .where { (Order.tableNo eq tableNo) and (Order.status eq OrderStatus.Pending) }.count()
                if (existingTable > 0) {
                    dialogProvider.setAlertComponent {
                        AlertUtils.buildError(
                            "Table $tableNo is already occupied",
                            onDismiss = {
                                dialogProvider.setAlertComponent {
                                    NewTableDialog(
                                        onDismiss = {
                                            dialogProvider.dismiss()
                                        },
                                        onConfirm = { tableNo, customerName, additionalNotes ->
                                            handleNewTable(tableNo, customerName, additionalNotes)
                                        }
                                    )
                                }.show()
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
    }

    @Composable
    fun newTableDialog () {
        NewTableDialog(
            onDismiss = {
                dialogProvider.dismiss()
            },
            onConfirm = { tableNo, customerName, additionalNotes ->
                handleNewTable(tableNo, customerName, additionalNotes)
            }
        )
    }


    windowScope.MenuBar {
        Menu("Casigma", mnemonic = 'A') {
            if (authenticatedUser.value !== null) {
                Item("Logout", onClick = {
                    dialogProvider.setAlertComponent {
                        AlertUtils.buildPromptDecision(
                            "Are you sure you want to logout?",
                            onConfirm = {
                                authenticatedUser.value = null
                                navHostController.navigate(AppScreen.Login.name)
                            }
                        )
                    }
                })
            }
            Item("Settings", onClick = {
                dialogProvider.setAlertComponent { SettingDialog(
                    onDismiss = {},
                    onConfirm = { dbDriver, dbUrl, dbUsername, dbPassword ->
                        settingRepository.setDbUrl(dbUrl)
                        settingRepository.setDbDriver(dbDriver)
                        settingRepository.setDbUsername(dbUsername)
                        settingRepository.setDbPassword(dbPassword)

                        try {
                            db.connect()
                            globalDatabaseConnected.value = true
                            if (globalLastDatabaseError.value != null) throw Exception(globalLastDatabaseError.value)
                            dialogProvider.setAlertComponent {
                                AlertUtils.buildSuccess("Database connection successful!")
                            }.show()
                        } catch (e: Exception) {
                            globalDatabaseConnected.value = false
                            globalLastDatabaseError.value = e.message

                            dialogProvider.setAlertComponent {
                                AlertUtils.buildError("Database connection failed! (Last error: ${e.message})")
                            }.show()
                        }
                    }
                ) }.show()
            })
            Item("Exit", onClick = {
                dialogProvider.setAlertComponent {
                    AlertUtils.buildPromptDecision(
                        "Are you sure you want to exit the app?",
                        onConfirm = {
                            exitProcess(0)
                        }
                    )
                }.show()
            }, shortcut = KeyShortcut(Key.Escape), mnemonic = 'E')
        }
        if (authenticatedUser.value != null) {
            if (authenticatedUser.value!!.role == UserRole.Waiters || authenticatedUser.value!!.role == UserRole.Admin) {
                Menu("Orders", mnemonic = 'O') {
                    Item("New", onClick = {
                        dialogProvider.setAlertComponent { newTableDialog() }.show()
                    })
                    Item("Menu", onClick = {
                        navHostController.navigate(AppScreen.WaitersRecordOrder.name)
                    })
                    Item("View Orders", onClick = {
                        navHostController.navigate(AppScreen.WaitersOrderList.name)
                    })
                }
            }

            if (authenticatedUser.value!!.role == UserRole.Chef || authenticatedUser.value!!.role == UserRole.Admin) {
                Menu("Chef", mnemonic = 'C') {
                    Item("View Orders", onClick = {
                        navHostController.navigate(AppScreen.ChefOrderList.name)
                    })
                }
            }

            if (authenticatedUser.value!!.role == UserRole.Cashier || authenticatedUser.value!!.role == UserRole.Admin) {
                Menu("Cashier", mnemonic = 'C') {
                    Item("View Orders", onClick = {
                        navHostController.navigate(AppScreen.CashierOrderList.name)
                    })
                }
            }
        }
    }

}