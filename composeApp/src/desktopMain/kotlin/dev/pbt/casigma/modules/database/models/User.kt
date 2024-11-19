package dev.pbt.casigma.modules.database.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

enum class UserRole(val role: String) {
    Admin("admin"),
    Waiters("waiters"),
    Cashier("cashier"),
    Chef("chef")
}

object User : IntIdTable("users") {
    val email = varchar("email", 50).uniqueIndex()
    val name = varchar("name", 50)
    val role = enumeration("role", UserRole::class)
    val password = text("password")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}