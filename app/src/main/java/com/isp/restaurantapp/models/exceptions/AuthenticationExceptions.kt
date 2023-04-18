package com.isp.restaurantapp.models.exceptions

class NotAuthenticatedException (message: String = "User is not authenticated") :Exception(message)
class UsernameNotValidException(): Exception(
    "Email is not in valid format"
)
class PasswordNotValidException(): Exception(
    "Password must contain at least 8 characters, 1 upper case, 1 lower case, 1 digit"
)
class PasswordsDontMatchException(): Exception(
    "Passwords dont match"
)

