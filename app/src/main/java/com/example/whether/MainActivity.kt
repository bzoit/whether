package com.example.whether

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    lateinit var shared: SharedPreferences

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.settingsList)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        val items = arrayOf(
            "Celsius", "Fahrenheit"
        )

        val systems = arrayOf(
            "Metric", "Imperial"
        )

        for (i in 0..1) {
            data.add(ItemsViewModel(items[i]))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = SettingsAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        shared = getSharedPreferences("CityDB", Context.MODE_PRIVATE)

        adapter.setOnItemClickListener(object: SettingsAdapter.onItemClickListener {
            override fun onItemClick(pos: Int) {
                val edit = shared.edit()
                edit.putString("pref" , systems[pos] as String?)
                edit.apply()
                finish();
                startActivity(intent);
            }
        })

        GlobalScope.launch(Dispatchers.Default) {
            val code = shared.getString("code", Context.MODE_PRIVATE.toString()).toString()
            val cityText = shared.getString("cityText", Context.MODE_PRIVATE.toString()).toString()
            var pref = shared.getString("pref", Context.MODE_PRIVATE.toString()).toString()

            if (code == "0") {
                startActivity(Intent(this@MainActivity, CitiesActivity::class.java))
            }

            if(pref === "0") {
                val edit = shared.edit()
                edit.putString("pref" , "Imperial")
                edit.apply()
                pref = shared.getString("pref", Context.MODE_PRIVATE.toString()).toString()
            }

            val currentURL = "https://dataservice.accuweather.com/currentconditions/v1/$code?apikey=${BuildConfig.W_KEY}&details=true"

            val forecastURL = "https://dataservice.accuweather.com/forecasts/v1/daily/1day/$code?apikey=${BuildConfig.W_KEY}&details=true"

            val currentJSON = URL(currentURL).readText()
            val forecastJSON = URL(forecastURL).readText()

            val currentObj = JSONArray(currentJSON)

            launch(Dispatchers.Main) {
                editLayout(cityText, currentObj, JSONObject(forecastJSON), pref)
            }
        }
    }

    private fun editLayout(t: String, c: JSONArray, f: JSONObject, p: String) {
        val current = c.getJSONObject(0)
        val temperatureObj = current.getJSONObject("Temperature").getJSONObject(p)
        val temp =  "${temperatureObj.getString("Value").toDouble().roundToInt()}° ${temperatureObj.getString("Unit")}"
        val forecast = f.getJSONArray("DailyForecasts").getJSONObject(0)

        val minimum = forecast.getJSONObject("Temperature").getJSONObject("Minimum").getString("Value").toDouble().roundToInt()
        val maximum = forecast.getJSONObject("Temperature").getJSONObject("Maximum").getString("Value").toDouble().roundToInt()

        val rise = forecast.getJSONObject("Sun").getString("Rise")
        val set = forecast.getJSONObject("Sun").getString("Set")

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
        tempView.text = temp

        println(p)

        if (p == "Metric") {
            lowView.text = ((minimum - 32) * 0.55555555556).roundToInt().toString() + "°"
            highView.text = ((maximum - 32) * 0.55555555556).roundToInt().toString() + "°"
        } else {
            lowView.text = minimum.toString() + "°"
            highView.text = maximum.toString() + "°"
        }

        sunriseView.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(rise)
        sunsetView.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(set)

        val description = current.getString("WeatherText")

        val capitalized = description.split(" ")
            .joinToString(separator = " ", transform = String::capitalize)

        descriptionView.text = capitalized
    }

    fun switchAway(v: View?) {
        startActivity(Intent(this@MainActivity, CitiesActivity::class.java))
    }

    fun listVis(v: View?) {
        val list = findViewById<RecyclerView>(R.id.settingsList)
        if(list.visibility == View.VISIBLE) {
            list.visibility = View.GONE
        } else {
            list.visibility = View.VISIBLE
        }
    }
}