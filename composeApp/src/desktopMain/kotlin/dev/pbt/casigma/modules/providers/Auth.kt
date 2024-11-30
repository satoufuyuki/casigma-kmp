package dev.pbt.casigma.modules.providers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.User
import dev.pbt.casigma.modules.database.models.UserObject
import dev.pbt.casigma.modules.database.models.UserRole
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class Auth(private val db: Database, private val argon2: Argon2, private val authenticatedUser: MutableState<UserObject?>) {
    fun authenticate(username: String, password: String): Boolean {
        return transaction(db.connect()) {
            val user = User.selectAll().where {
                User.username eq username
            }.firstOrNull()

            if (user == null) {
                return@transaction false
            }

            val isPasswordValid = argon2.verify(password, user[User.password])
            if (!isPasswordValid) {
                return@transaction false
            }

            authenticatedUser.value = UserObject(
                id = user[User.id].value,
                username = user[User.username],
                name = user[User.name],
                role = UserRole.valueOf(user[User.role].name),
                password = user[User.password],
                createdAt = user[User.createdAt]
            )

            return@transaction true
        }
    }
}