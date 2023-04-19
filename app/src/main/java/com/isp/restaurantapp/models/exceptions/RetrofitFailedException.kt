package com.isp.restaurantapp.models.exceptions

class RetrofitFailedException(override val message: String="Retrofit action failed"): Exception()