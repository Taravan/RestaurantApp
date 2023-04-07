package com.isp.restaurantapp.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app


open class MyFrb {
    companion object {
        val db: FirebaseFirestore by lazy {
            Firebase.firestore
        }
    }
}