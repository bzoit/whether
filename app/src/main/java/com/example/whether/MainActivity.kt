package com.example.whether

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.ULocale.getCountry
import android.os.Build
import android.os.Bundle
import android.provider.Settings.System.DATE_FORMAT
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var shared : SharedPreferences

    lateinit var settingsLV: ListView

    lateinit var listAdapter: ArrayAdapter<String>

    lateinit var settingsList: ArrayList<String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsLV = findViewById(R.id.settingsList)

        settingsList = ArrayList()
        settingsList.add("Celsius")
        settingsList.add("Fahrenheit")

        listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            settingsList
        )

        settingsLV.adapter = listAdapter

        GlobalScope.launch(Dispatchers.Default) {
            shared = getSharedPreferences("CityDB", Context.MODE_PRIVATE)
            val code = shared.getString("code", Context.MODE_PRIVATE.toString()).toString()
            val cityText = shared.getString("cityText", Context.MODE_PRIVATE.toString()).toString()

            if (code == "0") {
                startActivity(Intent(this@MainActivity, CitiesActivity::class.java))
            }

            val currentURL = "https://dataservice.accuweather.com/currentconditions/v1/$code?apikey=${BuildConfig.W_KEY}&details=true"

            val forecastURL = "https://dataservice.accuweather.com/forecasts/v1/daily/1day/$code?apikey=${BuildConfig.W_KEY}&details=true"

            val currentJSON = URL(currentURL).readText()
            val forecastJSON = URL(forecastURL).readText()

            val currentObj = JSONArray(currentJSON)
            val forecastObj = JSONObject(forecastJSON)

            launch(Dispatchers.Main) {
                editLayout(cityText, currentObj, forecastObj)
            }
        }
    }

    private fun editLayout(t: String, c: JSONArray, f: JSONObject) {
        val current = c.getJSONObject(0)
        println(current)

        val locationView = findViewById<TextView>(R.id.location)
        val descriptionView = findViewById<TextView>(R.id.status)
        val tempView = findViewById<TextView>(R.id.temp)
        val lowView = findViewById<TextView>(R.id.low)
        val highView = findViewById<TextView>(R.id.high)
        val sunriseView = findViewById<TextView>(R.id.sunrise)
        val sunsetView = findViewById<TextView>(R.id.sunset)
        val windView = findViewById<TextView>(R.id.wind)
        val uvView = findViewById<TextView>(R.id.uv)
        val humidityView = findViewById<TextView>(R.id.humidity)
        val pressureView = findViewById<TextView>(R.id.pressure)

        locationView.text = t

        val description = current.getString("WeatherText")

        val capitalized = description.split(" ")
            .joinToString(separator = " ", transform = String::capitalize)

        descriptionView.text = capitalized
    }

    fun switchAway(v: View?) {
        startActivity(Intent(this@MainActivity, CitiesActivity::class.java))
    }

    fun promptUser () {

    }
}