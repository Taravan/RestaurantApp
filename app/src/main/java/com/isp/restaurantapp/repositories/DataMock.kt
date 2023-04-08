package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.Category
import com.isp.restaurantapp.models.MenuItem
import com.isp.restaurantapp.models.Table

class DataMock: TableGetterService{

    private val tables = listOf(
        Table(0, 5, "kodStoluCisloPet"),
        Table(1, 10, "kodStoluCisloDeset"),
        Table(2, 15, "kodStoluCisloPatnact")
    )

    private val categories = listOf(
        Category(0, "Pečivo"),
        Category(1, "Uzeniny")
    )

    private val menuItems = listOf(
        MenuItem(0, "Rohlik", "Toto je rohlik.", 0, "Pečivo", 5.50),
        MenuItem(1, "Klobása", "Toto je klobasa.", 1, "Uzeniny",35.90),
        MenuItem(2, "Párek", "Toto je párek.", 1, "Uzeniny", 20.00),
        MenuItem(3, "Houska", "Toto je houska.", 0, "Pečivo", 6.50),
        MenuItem(4, "Kobliha", "Toto je kobliha.", 0, "Pečivo", 10.90)
    )

    private val unpaidMenuItems = listOf(
        MenuItem(0, "Rohlik", "Toto je rohlik.", 0, "Pečivo", 5.50),
        MenuItem(4, "Kobliha", "Toto je kobliha.", 0, "Pečivo", 10.90)
    )
/*
    getTables(): List<Table> {
        return tables
    }
*/
    fun getTableById(id: Int): Table{
        return tables.find { it.id == id }!!
    }

    fun getCategories(): List<Category>{
        return categories
    }

    fun getItems(): List<MenuItem>{
        return menuItems
    }

    fun getItemsById(id: Int): List<MenuItem> {
        return menuItems.filter { it.categoryId == id }
    }

    fun getUnpaidItems(): List<MenuItem> {
        return  unpaidMenuItems
    }

    override suspend fun getTables(): List<Table> {
        return tables
    }

}