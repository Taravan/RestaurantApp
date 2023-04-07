package com.isp.restaurantapp.models.exceptions

class NotAuthenticatedException (message: String = "User is not authenticated")
    :Exception(message){
}