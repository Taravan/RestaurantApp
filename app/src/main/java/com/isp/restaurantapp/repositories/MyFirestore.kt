package com.isp.restaurantapp.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


open class MyFirestore {
    companion object {
        /**
         * Singleton instance of [FirebaseFirestore]
         */
        val firestore: FirebaseFirestore by lazy {
            Firebase.firestore  //singleton instance (= .getInstance())
        }
    }
}