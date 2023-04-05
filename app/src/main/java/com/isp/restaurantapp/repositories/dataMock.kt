package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.Category
import com.isp.restaurantapp.models.Item
import com.isp.restaurantapp.models.Table

class dataMock {

    private val tables = listOf(
        Table(0, 5, "kodStoluCisloPet"),
        Table(1, 10, "kodStoluCisloDeset"),
        Table(2, 15, "kodStoluCisloPatnact")
    )

    private val categories = listOf(
        Category(0, "Pečivo"),
        Category(1, "Uzeniny")
    )

    private val items = listOf(
        Item(0, "Rohlik", "Toto je rohlik.", 0, 5.50),
        Item(1, "Klobása", "Toto je klobasa.", 1, 35.90),
        Item(0, "Párek", "Toto je párek.", 1, 20.00),
        Item(0, "Houska", "Toto je houska.", 0, 6.50),
        Item(0, "Kobliha", "Toto je kobliha.", 0, 10.90)
    )

    fun getTables(): List<Table> {
        return tables
    }

    fun getTableById(id: Int): Table{
        return tables.find { it.id == id }!!
    }

    fun getCategories(): List<Category>{
        return categories
    }

    fun getItems(): List<Item>{
        return items
    }

    fun getItemsById(id: Int): List<Item> {
        return items.filter { it.categoryId == id }
    }

}