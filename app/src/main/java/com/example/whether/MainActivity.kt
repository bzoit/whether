package com.example.whether

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.view.View

class MainActivity : AppCompatActivity() {

    lateinit var shared : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //shared = getSharedPreferences("CityDB" , Context.MODE_PRIVATE)
        //val city = shared.getString("city", Context.MODE_PRIVATE)
        //println(city)
    }

    fun switchAway(v: View?) {
        startActivity(Intent(this@MainActivity, CitiesActivity::class.java))
    }
}