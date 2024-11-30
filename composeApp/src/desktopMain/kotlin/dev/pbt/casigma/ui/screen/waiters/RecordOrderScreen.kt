package dev.pbt.casigma.ui.screen.waiters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.pbt.casigma.GlobalContext
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.Menu
import dev.pbt.casigma.modules.database.models.MenuCategory
import dev.pbt.casigma.modules.database.models.MenuObject
import dev.pbt.casigma.modules.database.models.Order
import dev.pbt.casigma.modules.database.models.OrderItemObject
import dev.pbt.casigma.modules.database.models.OrderObject
import dev.pbt.casigma.modules.database.models.OrderStatus
import dev.pbt.casigma.modules.providers.DialogProvider
import dev.pbt.casigma.modules.utils.AlertUtils
import dev.pbt.casigma.modules.utils.OrderUtils
import dev.pbt.casigma.modules.utils.TextUtils
import dev.pbt.casigma.modules.viewmodel.MenuViewModel
import dev.pbt.casigma.modules.viewmodel.TableViewModel
import dev.pbt.casigma.ui.components.DropdownTable
import dev.pbt.casigma.ui.components.MenuCard
import dev.pbt.casigma.ui.components.OrderDetail
import dev.pbt.casigma.ui.screen.ScreenBase
import dev.pbt.casigma.ui.theme.neutral
import dev.pbt.casigma.ui.theme.white
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named
import java.util.*

class RecordOrderScreen(override val route: String): ScreenBase(route) {
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun render(
    ) {
        val globalManagedOrderId: MutableState<Int?> = koinInject(qualifier = named(GlobalContext.ManagedOrderId))
        val dialogProvider: DialogProvider = koinInject()
        val menuViewModel: MenuViewModel = koinViewModel()
        val tableViewModel: TableViewModel = koinViewModel()
        val menus by menuViewModel.menus.collectAsState()
        val menuScrollState = rememberScrollState()
        val tables by tableViewModel.tableState.collectAsState()
        val menuCategory = listOf(MenuCategory.Food, MenuCategory.Beverages, MenuCategory.Dessert)
        val currentActiveCategory = remember { mutableStateOf(0) }
        val currentActiveOrderId = remember { mutableStateOf<Int?>(null) }
        val menuItems by remember {
            derivedStateOf {
                menus.filter { it.category.ordinal == currentActiveCategory.value }.map { menu ->
                    val currentOrder = tables.find { it.id == currentActiveOrderId.value }
                    val quantity = currentOrder?.items?.find { it.menu.id == menu.id }?.quantity ?: 0
                    menu to quantity
                }
            }
        }

        val saveOrder = {
            currentActiveOrderId.value?.let {
                try {
                    tableViewModel.saveOrder(it)
                    dialogProvider.setAlertComponent {
                        AlertUtils.buildSuccess("Order saved successfully!")
                    }.show()
                } catch (e: Exception) {
                    dialogProvider.setAlertComponent {
                        AlertUtils.buildError("Failed to save order: ${e.message}")
                    }.show()
                }
            }
        }

        val handleTableSelection = { tableIndex: Int ->
            tables.getOrNull(tableIndex)?.let {
                currentActiveOrderId.value = it.id
            }
        }

        if (tables.isEmpty()) {
            tableViewModel.fetchOrder(OrderStatus.Pending)
        } else {
            // Check previous global managed order
            globalManagedOrderId.value?.let { orderId ->
                globalManagedOrderId.value = null // clear at first boot
                val index = tables.indexOf(tables.find { it.id == orderId })
                if (index != -1) {
                    handleTableSelection(index)
                }
            }
        }

        // Container for the view
        Row(
            modifier = Modifier.fillMaxWidth().background(neutral),
            horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.Start),
            verticalAlignment = Alignment.Top
        ) {
            // Column for the left side of the view
            Column(
                modifier = Modifier.fillMaxWidth(0.75f).padding(48.dp, 48.dp, 0.dp, 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
            ) {
                // Title
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Please select the menu!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 40.sp
                    )
                    DropdownTable(
                        tables = tables.map { it.tableNo.toString() },
                        currentTableIndex = tables.indexOf(tables.find { it.id == currentActiveOrderId.value }),
                        onTableSelected = {
                            handleTableSelection(it)
                        }
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                ) {
                    menuCategory.forEachIndexed { _, category ->
                        TextButton(onClick = { currentActiveCategory.value = category.ordinal }) {
                            Text(
                                TextUtils.toTitleCase(category.category),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 25.sp,
                                color = if (currentActiveCategory.value == category.ordinal) Color.Black else Color.Gray,
                            )
                        }
                    }
                }
                // Menu Card
                if (currentActiveOrderId.value != null) { // Don't show menu if no table is selected
                    FlowRow (
                        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start),
                        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
                        modifier = Modifier.fillMaxWidth().verticalScroll(menuScrollState),
                        overflow = FlowRowOverflow.Visible
                    ) {
                        menuItems.forEach { (it, quantity) ->
                            MenuCard(
                                name = it.name,
                                price = it.price,
                                image = it.image,
                                onAdd = { tableViewModel.incrementOrderItemQuantity(currentActiveOrderId.value!!, it) },
                                onRemove = { tableViewModel.decrementOrderItemQuantity(currentActiveOrderId.value!!, it.id) },
                                quantity = quantity
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Please select a table to start recording!",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            fontSize = 30.sp
                        )
                    }
                }
            }
            Column (
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(white)
            ) {
                OrderDetail(
                    tables.find { it.id == currentActiveOrderId.value },
                    buttonAction = { saveOrder()}
                )
            }
        }
    }
}

