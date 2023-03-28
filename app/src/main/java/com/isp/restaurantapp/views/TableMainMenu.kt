package com.isp.restaurantapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.isp.restaurantapp.R

class TableMainMenu : AppCompatActivity() {

    lateinit var tv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_main_menu)


        tv = findViewById(R.id.tFirst)

        tv.text = intent.getStringExtra("text")
    }
}