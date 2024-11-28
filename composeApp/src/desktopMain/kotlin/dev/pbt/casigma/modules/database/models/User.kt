package dev.pbt.casigma.modules.database.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

enum class UserRole(val role: String) {
    Admin("admin"),
    Waiters("waiters"),
    Cashier("cashier"),
    Chef("chef")
}

data class UserObject(
    val id: Int,
    val username: String,
    val name: String,
    val role: UserRole,
    val password: String,
    val createdAt: LocalDateTime
)

object User : IntIdTable("users") {
    val username = varchar("username", 50).uniqueIndex()
    val name = varchar("name", 50)
    val role = enumeration("role", UserRole::class)
    val password = text("password")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}