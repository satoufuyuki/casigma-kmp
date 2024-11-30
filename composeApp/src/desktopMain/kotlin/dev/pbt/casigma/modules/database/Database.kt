package dev.pbt.casigma.modules.database

import androidx.compose.runtime.MutableState
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.pbt.casigma.modules.datastore.SettingRepository

class Database(private val settingRepository: SettingRepository, private val lastDatabaseError: MutableState<String?>, private val databaseConnected: MutableState<Boolean>) {
    fun connect(): org.jetbrains.exposed.sql.Database? {
        lastDatabaseError.value = null
        try {
            Class.forName(settingRepository.dbDriver)
        } catch (e: ClassNotFoundException) {
            lastDatabaseError.value = "Database driver not found"
            e.printStackTrace()
        }

        try {
            val config = HikariConfig()
            config.jdbcUrl = settingRepository.dbUrl
            config.username = settingRepository.dbUsername
            config.password = settingRepository.dbPassword
            config.driverClassName = settingRepository.dbDriver

            val dataSource = HikariDataSource(config)
            val connection = org.jetbrains.exposed.sql.Database.connect(dataSource)

            if (dataSource.connection.isValid(5)) databaseConnected.value = true
            else throw Exception("Database connection failed (5s timeout exceededx)")

            return connection
        } catch (e: Exception) {
            e.printStackTrace()
            lastDatabaseError.value = e.message
        }

        return null
    }
}