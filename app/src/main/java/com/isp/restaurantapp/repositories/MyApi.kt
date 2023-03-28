package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.BaseModel
import com.isp.restaurantapp.models.Table
import retrofit2.Call
import retrofit2.http.*

interface MyApi {

    // Const defined in build.gradle as a config variable
    // Interface which uses Retrofit to make an API Call
    // Response is mapped to List<Table>
    // Table model property names MUST MATCH with Json' variables
    @Headers(BuildConfig.API_KEY_HEADER)
    @GET(BuildConfig.SELECT_TABLES)
    suspend fun getTables(): List<Table>

    //Put another Api call methods here...
    // e.g.
    // @GET(BuildConfig.SELECT_USERS)  // const target -> url_suffix.php saved in build.gradle
    // suspend fun getUsers(): List<User>
}