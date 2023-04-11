package com.isp.restaurantapp.models

import com.isp.restaurantapp.models.dto.GoodsItemDTO

/**
 * Class for mapping categories in pager
 */
data class ItemCategory(
    val categoryName: String = "",
    val categoryItems: List<GoodsItemDTO>
    )
