package dev.pbt.casigma.ui.screen.waiters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.Menu
import dev.pbt.casigma.modules.database.models.MenuCategory
import dev.pbt.casigma.modules.database.models.MenuObject
import dev.pbt.casigma.modules.database.models.Order
import dev.pbt.casigma.modules.database.models.OrderItemObject
import dev.pbt.casigma.modules.database.models.OrderObject
import dev.pbt.casigma.modules.database.models.OrderStatus
import dev.pbt.casigma.modules.utils.OrderUtils
import dev.pbt.casigma.ui.components.DropdownTable
import dev.pbt.casigma.ui.components.MenuCard
import dev.pbt.casigma.ui.components.OrderDetail
import dev.pbt.casigma.ui.screen.ScreenBase
import dev.pbt.casigma.ui.theme.neutral
import dev.pbt.casigma.ui.theme.white
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class RecordOrderScreen(override val route: String, private val db: Database, private val orderUtils: OrderUtils): ScreenBase(route) {
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun render() {
        val menus = remember {
            mutableStateListOf<MenuObject>()
        }

        val availableTables = remember {
            mutableStateListOf<Int>()
        }

        val currentOrder: MutableState<OrderObject?> = remember {
            mutableStateOf(null)
        }

        val currentOrderItems = remember {
            mutableStateListOf<OrderItemObject>()
        }

        val currentActiveTable = remember {
            mutableStateOf(-1)
        }

        transaction(db.conn) {
            if (menus.isEmpty()) {
                Menu.selectAll().forEach {
                    menus.add(
                        MenuObject(
                            id = it[Menu.id].value,
                            name = it[Menu.name],
                            price = it[Menu.price],
                            category = it[Menu.category],
                            createdAt = it[Menu.createdAt],
                            image = it[Menu.image]
                        )
                    )
                }
            }

            if (availableTables.isEmpty()) {
                Order.select(Order.tableNo).forEach {
                    availableTables.add(it[Order.tableNo])
                }
            }
        }

        val menuScrollState = rememberScrollState()
        val currentActiveCategory = remember {
            mutableStateOf(0)
        }

        val menuCategory = listOf(MenuCategory.Food, MenuCategory.Beverages, MenuCategory.Dessert)

        fun handleQuantityButton(action: String, item: MenuObject) {
            if (currentOrder.value == null) {
                return
            }

            var existingItem = currentOrder.value?.items?.find { it.menu.id == item.id }
            when (action) {
                "add" -> {
                   if (existingItem != null) {
                       val index = currentOrderItems.indexOf(existingItem)
                       currentOrderItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
                   } else {
                       currentOrder.value?.items?.add(
                           OrderItemObject(
                               id = 0,
                               quantity = 1,
                               menu = item
                           )
                       )
                   }
                }
                "remove" -> {
                    if (existingItem != null) {
                        if (existingItem.quantity > 1) {
                            val index = currentOrderItems.indexOf(existingItem)
                            currentOrderItems[index] = existingItem.copy(quantity = existingItem.quantity - 1)
                        } else {
                            currentOrderItems.remove(existingItem)
                        }
                    }
                }
            }
        }

        fun handleTableSelection(tableIndex: Int) {
            currentOrder.value = orderUtils.fetchOrders(OrderStatus.Pending)[tableIndex]
            currentActiveTable.value = tableIndex
            currentOrderItems.clear()
            currentOrderItems.addAll(currentOrder.value?.items ?: listOf())
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
                        tables = availableTables.map { "$it" },
                        currentTableIndex = currentActiveTable.value,
                        onTableSelected = {
                            handleTableSelection(it)
                        }
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                ) {
                    menuCategory.forEachIndexed { index, category ->
                        TextButton(onClick = { currentActiveCategory.value = index }) {
                            Text(
                                category.category,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 25.sp,
                                color = if (currentActiveCategory.value == index) Color.Black else Color.Gray,
                            )
                        }
                    }
                }
                // Menu Card
                FlowRow (
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start),
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
                    modifier = Modifier.fillMaxWidth().verticalScroll(menuScrollState),
                    overflow = FlowRowOverflow.Visible
                ) {
                    menus.forEach {
                        if (it.category == menuCategory[currentActiveCategory.value]) {
                            MenuCard(
                                name = it.name,
                                price = it.price,
                                image = it.image,
                                onAdd = { handleQuantityButton("add", it) },
                                onRemove = { handleQuantityButton("remove", it) },
                                quantity = currentOrderItems.find { item -> item.menu.id == it.id }?.quantity ?: 0
                            )
                        }
                    }
                }
            }
            Column (
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(white)
            ) {
                OrderDetail()
            }
        }
    }
}