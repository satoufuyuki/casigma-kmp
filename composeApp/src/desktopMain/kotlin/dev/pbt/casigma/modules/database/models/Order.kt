package dev.pbt.casigma.modules.database.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

enum class OrderStatus(val status: String) {
    Pending("pending"),
    Completed("completed"),
    Cancelled("confirmed")
}

object Order : IntIdTable("orders") {
    val customerName: Column<String> = text("customer_name")
    val status: Column<OrderStatus> = enumeration("status", OrderStatus::class)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val additionalNotes: Column<String> = text("additional_notes")
}

object OrderItem : IntIdTable("order_items") {
    val quantity = integer("quantity").default(0)
    val menuId = integer("menu_id").references(Menu.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val orderId = integer("order_id").references(Order.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}