package dev.pbt.casigma.modules.database.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class MenuObject(
    val id: Int,
    val name: String,
    val price: Float,
    val category: MenuCategory,
    val image: String,
    val createdAt: LocalDateTime
)

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