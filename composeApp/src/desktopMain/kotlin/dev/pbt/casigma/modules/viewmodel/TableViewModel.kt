package dev.pbt.casigma.modules.viewmodel

import androidx.lifecycle.ViewModel
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.*
import dev.pbt.casigma.modules.utils.OrderUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TableViewModel(private val db: Database, private val orderUtils: OrderUtils) : ViewModel() {
    private val _tableState = MutableStateFlow<List<OrderObject>>(emptyList())
    val tableState = _tableState.asStateFlow()

    fun saveOrder(orderId: Int) {
        transaction(db.conn) {
            val order = _tableState.value.find { it.id == orderId } ?: throw IllegalArgumentException("Order not found")
            // Check for new items
            order.items.filter { it.id == null }.forEach { item ->
                val newId = OrderItem.insert {
                    it[quantity] = item.quantity ?: 0
                    it[menuId] = item.menu.id
                    it[OrderItem.orderId] = orderId
                }
                _tableState.update { orders ->
                    orders.map { order ->
                        if (order.id == orderId) {
                            order.copy(items = order.items.map { item ->
                                if (item.id == null) {
                                    println("New item added: ${item.menu.name}")
                                    item.copy(id = newId[OrderItem.id].value)
                                } else {
                                    item
                                }
                            })
                        } else {
                            order
                        }
                    }
                }
            }

            // Check for pending deletion
            order.items.filter { (it.quantity == null) and (it.id !== null) }.forEach { item ->
                OrderItem.deleteWhere { OrderItem.id eq item.id }
                _tableState.update { orders ->
                    orders.map { order ->
                        order.copy(items = order.items.filter { it.id != item.id })
                    }
                }
            }

            // The rest
            order.items.filter { (it.quantity !== null) and (it.id !== null) }.forEach { item ->
                OrderItem.update({ OrderItem.id eq item.id }) {
                    it[quantity] = item.quantity ?: 0
                }
            }
        }
    }

    fun fetchOrder(status: OrderStatus) {
        _tableState.update { orderUtils.fetchOrders(status).values.toList() }
    }

    fun incrementOrderItemQuantity(orderId: Int, menu: MenuObject) {
        _tableState.update { orders ->
            orders.map { order ->
                if (order.id != orderId) return@map order
                if (order.items.find { it.menu.id == menu.id } == null || order.items.isEmpty()) {
                    val orderItem = OrderItemObject(
                        id = null,
                        quantity = 1,
                        menu = menu
                    )
                    order.copy(items = order.items + orderItem)
                } else {
                    order.copy(
                        items = order.items.map { item ->
                            if (item.menu.id == menu.id) {
                                item.copy(quantity = (item.quantity ?: 0).plus(1))
                            } else {
                                item
                            }
                        }
                    )
                }
            }
        }
    }

    fun decrementOrderItemQuantity(orderId: Int, itemId: Int) {
        _tableState.update { orders ->
            orders.map { order ->
                if (order.id != orderId) return@map order
                order.copy(
                    items = order.items.map { item ->
                        if (item.menu.id == itemId) {
                            if (((item.quantity?.minus(1)) ?: 0) <= 0) item.copy(quantity = null)
                            else item.copy(quantity = item.quantity?.minus(1))
                        } else {
                            item
                        }
                    }
                )
            }
        }
    }

    // Remove an order by ID
    fun removeOrder(orderId: Int) {
        transaction(db.conn) {
            Order.deleteWhere { Order.id eq orderId }
            _tableState.value = _tableState.value.filter { it.id != orderId }
        }
    }

    fun updateOrderStatus(orderId: Int, status: OrderStatus) {
        transaction(db.conn) {
            Order.update({
                Order.id eq orderId
            }) {
                it[Order.status] = status
            }
            _tableState.update { orders ->
                orders.map { order ->
                    if (order.id == orderId) {
                        order.copy(status = status)
                    } else {
                        order
                    }
                }
            }
        }
    }
}
