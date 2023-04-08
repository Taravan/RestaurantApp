package com.isp.restaurantapp.models

data class MenuItem(val id: Int, val name: String, val description: String, val categoryId: Int,
                    val categoryName: String, val price: Double)
