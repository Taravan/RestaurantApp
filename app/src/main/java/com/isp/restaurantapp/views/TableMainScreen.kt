package com.isp.restaurantapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.isp.restaurantapp.R

class TableMainScreen : AppCompatActivity() {

    lateinit var tv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_main_screen)


        tv = findViewById(R.id.tFirst)

        tv.text = intent.getStringExtra("text")
    }
}