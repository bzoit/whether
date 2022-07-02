package com.example.whether

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    lateinit var shared : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shared = getSharedPreferences("CityDB" , Context.MODE_PRIVATE)
        val city = shared.getString("city", Context.MODE_PRIVATE.toString())

        if(city.isNullOrEmpty()) {
            println("no city")
            startActivity(Intent(this@MainActivity, CitiesActivity::class.java))
        } else {
            println(city)
        }

        val api = BuildConfig.OWM_KEY
    }

    fun switchAway(v: View?) {
        startActivity(Intent(this@MainActivity, CitiesActivity::class.java))
    }
}