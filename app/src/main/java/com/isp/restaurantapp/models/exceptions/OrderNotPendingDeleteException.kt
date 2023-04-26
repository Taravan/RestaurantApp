package com.isp.restaurantapp.models.exceptions

class OrderNotPendingDeleteException: Exception("Cannot delete non-pending order")