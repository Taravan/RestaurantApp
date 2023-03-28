package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.BuildConfig
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitService(private val url: String = BuildConfig.API_URL){

    private val _apiService: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: MyApi by lazy {
        _apiService.create(MyApi::class.java)  // instantiate api interface
    }
}