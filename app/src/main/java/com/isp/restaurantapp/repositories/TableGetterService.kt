package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.Table
import retrofit2.http.*

interface TableGetterService {

    // Const defined in build.gradle as a config variable
    // Interface which uses Retrofit to make an API Call
    // Response is mapped to List<Table>
    // Table model property names MUST MATCH with Json' variables
    @Headers(BuildConfig.API_KEY_HEADER)
    @GET(BuildConfig.SELECT_TABLES)
    suspend fun getTables(): List<Table>

}