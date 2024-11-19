package dev.pbt.casigma.modules.database.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

enum class MenuCategory(val category: String) {
    Food("food"),
    Beverages("beverages"),
    Dessert("dessert")
}

object Menu : IntIdTable("menus") {
    val name = varchar("name", 50)
    val price = float("price")
    val category = enumeration("category", MenuCategory::class)
    val image = varchar("image", 50)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}