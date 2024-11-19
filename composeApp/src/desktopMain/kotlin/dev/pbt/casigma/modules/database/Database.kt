package dev.pbt.casigma.modules.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

class Database {
    var db: DataSource = connect();
    val conn = org.jetbrains.exposed.sql.Database.connect(db)

    private fun connect(): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/casigma_kmp"
        config.username = "root"
        config.password = ""
        config.driverClassName = "com.mysql.cj.jdbc.Driver"

        return HikariDataSource(config)

    }
}