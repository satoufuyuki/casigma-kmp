package dev.pbt.casigma.modules.database

import androidx.compose.runtime.mutableStateOf
import dev.pbt.casigma.modules.database.models.Menu
import dev.pbt.casigma.modules.database.models.MenuCategory
import dev.pbt.casigma.modules.database.models.User
import dev.pbt.casigma.modules.database.models.UserRole
import dev.pbt.casigma.modules.datastore.SettingRepository
import dev.pbt.casigma.modules.providers.Argon2
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val settingRepository = SettingRepository()
    val db = Database(settingRepository, mutableStateOf(null), mutableStateOf(false))
    val argon2 = Argon2()
    try {
        transaction(db.connect()) {
            // Admin
            User.insertIgnore {
                it[username] = "admin"
                it[password] = argon2.hash("admin")
                it[role] = UserRole.Admin
                it[name] = "Admin"
            }

            // Waiters
            User.insertIgnore {
                it[username] = "waiters"
                it[password] = argon2.hash("waiters")
                it[role] = UserRole.Waiters
                it[name] = "Waiters"
            }

            // Cashiers
            User.insertIgnore {
                it[username] = "cashier"
                it[password] = argon2.hash("cashier")
                it[role] = UserRole.Cashier
                it[name] = "Cashier"
            }

            // Chefs
            User.insertIgnore {
                it[username] = "chef"
                it[password] = argon2.hash("chef")
                it[role] = UserRole.Chef
                it[name] = "Chef"
            }

            // Foods
            Menu.insert {
                it[name] = "Ayam Saus Madu"
                it[price] = 35000f
                it[category] = MenuCategory.Food
                it[image] = "https://s3.smkn1karawang.sch.id/casigma/images/ayam-saus-madu.png"
            }

            Menu.insert {
                it[name] = "Chicken Steak"
                it[price] = 45000f
                it[category] = MenuCategory.Food
                it[image] = "https://s3.smkn1karawang.sch.id/casigma/images/chicken-steak.png"
            }

            Menu.insert {
                it[name] = "Kentang Goreng"
                it[price] = 15000f
                it[category] = MenuCategory.Food
                it[image] = "https://s3.smkn1karawang.sch.id/casigma/images/kentang-goreng.png"
            }

            Menu.insert {
                it[name] = "Sup Jamur"
                it[price] = 25000f
                it[category] = MenuCategory.Food
                it[image] = "https://s3.smkn1karawang.sch.id/casigma/images/sup-jamur.png"
            }

            Menu.insert {
                it[name] = "Tahu Renyah"
                it[price] = 10000f
                it[category] = MenuCategory.Food
                it[image] = "https://s3.smkn1karawang.sch.id/casigma/images/tahu-renyah.png"
            }

            Menu.insert {
                it[name] = "Udang Crispy"
                it[price] = 20000f
                it[category] = MenuCategory.Food
                it[image] = "https://s3.smkn1karawang.sch.id/casigma/images/udang-crispy.png"
            }

            // Drinks
            Menu.insert {
                it[name] = "Lemon Tea"
                it[price] = 10000f
                it[category] = MenuCategory.Beverages
                it[image] = "https://s3.smkn1karawang.sch.id/casigma/images/lemon-tea.jpg"
            }
        }
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}