package dev.pbt.casigma.modules.datastore
import java.util.prefs.Preferences

class SettingRepository {
    private val preferences: Preferences = Preferences.userRoot().node(javaClass.name)
    companion object {
        val DB_DRIVER = "db_driver"
        val DB_URL = "db_url"
        val DB_USERNAME = "db_username"
        val DB_PASSWORD = "db_password"
    }

    val dbDriver get() = preferences.get(DB_DRIVER, "")
    val dbUrl get() = preferences.get(DB_URL, "")
    val dbUsername get() = preferences.get(DB_USERNAME, "")
    val dbPassword get() = preferences.get(DB_PASSWORD, "")

    fun setDbDriver(driver: String) {
        preferences.put(DB_DRIVER, driver)
    }

    fun setDbUrl(url: String) {
        preferences.put(DB_URL, url)
    }

    fun setDbUsername(username: String) {
        preferences.put(DB_USERNAME, username)
    }

    fun setDbPassword(password: String) {
        preferences.put(DB_PASSWORD, password)
    }
}