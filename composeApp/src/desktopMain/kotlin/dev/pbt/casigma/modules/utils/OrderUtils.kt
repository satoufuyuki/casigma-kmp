package dev.pbt.casigma.modules.utils

import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.Menu
import dev.pbt.casigma.modules.database.models.MenuObject
import dev.pbt.casigma.modules.database.models.Order
import dev.pbt.casigma.modules.database.models.OrderItem
import dev.pbt.casigma.modules.database.models.OrderItemObject
import dev.pbt.casigma.modules.database.models.OrderObject
import dev.pbt.casigma.modules.database.models.OrderStatus
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class OrderUtils(private val db: Database) {
    fun fetchOrder(orderId: Int): OrderObject? {
        var result: OrderObject? = null

        transaction (db.conn) {
            val query = Join(
                Order, OrderItem,
                onColumn = Order.id, otherColumn = OrderItem.orderId,
                joinType = JoinType.INNER,
            ).join(Menu, JoinType.INNER, OrderItem.menuId, Menu.id)

            val res = query.selectAll()
                .where { Order.id eq orderId }
                .toList()

            if (!res.any()) {
                return@transaction
            }

            val orderItems = arrayListOf<OrderItemObject>()
            res.forEach {
                if (it[Menu.id] != null) {
                    val menuItem = MenuObject(
                        id = it[Menu.id].value,
                        name = it[Menu.name],
                        price = it[Menu.price],
                        category = it[Menu.category],
                        image = it[Menu.image],
                        createdAt = it[Menu.createdAt]
                    )

                    orderItems.add(
                        OrderItemObject(
                            id = it[OrderItem.id].value,
                            quantity = it[OrderItem.quantity],
                            menu = menuItem
                        )
                    )
                }
            }

            result = OrderObject(
                id = orderId,
                createdAt = res.first()[Order.createdAt],
                additionalNotes = res.first()[Order.additionalNotes],
                tableNo = res.first()[Order.tableNo],
                items = orderItems,
                customerName = res.first()[Order.customerName],
                status = res.first()[Order.status],
            )
        }

        return result
    }

    fun fetchOrders(status: OrderStatus?): Map<Int, OrderObject> {
        val ordersMap = mutableMapOf<Int, OrderObject>()

        transaction (db.conn) {
            val query = Join(
                Order, OrderItem,
                onColumn = Order.id, otherColumn = OrderItem.orderId,
                joinType = JoinType.LEFT,
            ).join(Menu, JoinType.LEFT, OrderItem.menuId, Menu.id)

            var res: List<ResultRow> = arrayListOf()
            if (status != null) {
                res = query.selectAll()
                    .where { Order.status eq status }
                    .sortedBy { Order.createdAt }.toList()
            }  else {
                res = query.selectAll()
                    .sortedBy { Order.createdAt }.toList()
            }

            res.forEach {
                val orderId = it[Order.id].value
                if (it[Menu.id] != null) {
                    val menuItem = MenuObject(
                        id = it[Menu.id].value,
                        name = it[Menu.name],
                        price = it[Menu.price],
                        category = it[Menu.category],
                        image = it[Menu.image],
                        createdAt = it[Menu.createdAt]
                    )

                    val orderItem = OrderItemObject(
                        id = it[OrderItem.id].value,
                        quantity = it[OrderItem.quantity],
                        menu = menuItem
                    )

                    if (ordersMap.containsKey(orderId)) {
                        ordersMap[orderId]?.items = listOf(orderItem) + ordersMap[orderId]?.items!!
                    } else {
                        ordersMap[orderId] = OrderObject(
                            id = orderId,
                            createdAt = it[Order.createdAt],
                            additionalNotes = it[Order.additionalNotes],
                            tableNo = it[Order.tableNo],
                            items = arrayListOf(orderItem),
                            customerName = it[Order.customerName],
                            status = it[Order.status],
                        )
                    }
                } else {
                    ordersMap[orderId] = OrderObject(
                        id = orderId,
                        createdAt = it[Order.createdAt],
                        additionalNotes = it[Order.additionalNotes],
                        tableNo = it[Order.tableNo],
                        items = arrayListOf(),
                        customerName = it[Order.customerName],
                        status = it[Order.status],
                    )
                }
            }
        }

        return ordersMap
    }

}