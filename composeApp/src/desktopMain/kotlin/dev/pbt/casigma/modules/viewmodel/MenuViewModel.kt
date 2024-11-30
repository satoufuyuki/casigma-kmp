package dev.pbt.casigma.modules.viewmodel

import androidx.lifecycle.ViewModel
import dev.pbt.casigma.modules.database.Database
import dev.pbt.casigma.modules.database.models.Menu
import dev.pbt.casigma.modules.database.models.MenuObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MenuViewModel(private val db: Database) : ViewModel() {
    private val _menus = MutableStateFlow<List<MenuObject>>(emptyList())
    val menus: StateFlow<List<MenuObject>> = _menus.asStateFlow()

    init {
        loadMenus()
    }

    private fun loadMenus() {
        transaction(db.connect()) {
            val menuList = Menu.selectAll().map {
                MenuObject(
                    id = it[Menu.id].value,
                    name = it[Menu.name],
                    price = it[Menu.price],
                    category = it[Menu.category],
                    createdAt = it[Menu.createdAt],
                    image = it[Menu.image]
                )
            }
            _menus.value = menuList
        }
    }
}
