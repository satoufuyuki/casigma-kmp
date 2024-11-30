package dev.pbt.casigma.modules.database

import dev.pbt.casigma.modules.database.models.User
import dev.pbt.casigma.modules.database.models.UserRole
import dev.pbt.casigma.modules.providers.Argon2
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
//    val db = Database()
//    val argon2 = Argon2()
//    transaction(db.connect()) {
//        // Admin
//        User.insert {
//            it[username] = "admin"
//            it[password] = argon2.hash("admin")
//            it[role] = UserRole.Admin
//            it[name] = "Admin"
//        }
//
//        // Waiters
//        User.insert {
//            it[username] = "waiters"
//            it[password] = argon2.hash("waiters")
//            it[role] = UserRole.Waiters
//            it[name] = "Waiters"
//        }
//
//        // Cashiers
//        User.insert {
//            it[username] = "cashier"
//            it[password] = argon2.hash("cashier")
//            it[role] = UserRole.Cashier
//            it[name] = "Cashier"
//        }
//
//        // Chefs
//        User.insert {
//            it[username] = "chef"
//            it[password] = argon2.hash("chef")
//            it[role] = UserRole.Chef
//            it[name] = "Chef"
//        }
//    }
}