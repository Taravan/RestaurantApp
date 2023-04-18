package com.isp.restaurantapp.models.exceptions

class DocumentNotFoundException(message: String = "Document not found in firestore repository"):
    Exception(message) {
}