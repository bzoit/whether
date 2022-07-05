package com.example.whether

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CitiesActivity : AppCompatActivity() {

    lateinit var shared : SharedPreferences

    val cities = arrayOf(
        "Philadelphia", "Fairbanks"
    )

    val countries = arrayOf(
        "US", "US"
    )

    val states = arrayOf(
        "PA", "AK"
    )

    val codes = arrayOf(
        "350540", "346836"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        shared = getSharedPreferences("CityDB" , Context.MODE_PRIVATE)

        val citiesLV = findViewById<ListView>(R.id.citiesList)

        citiesLV.adapter = CustomAdapter(this)

        citiesLV.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val cityArr = selectedItem.split("&").toTypedArray()
            val edit = shared.edit()
            edit.putString("code" , cityArr[0] as String?)
            edit.putString("cityText", cityArr[1] as String?)
            edit.apply()
            startActivity(Intent(this@CitiesActivity, MainActivity::class.java))
        }
    }

    private class CustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context = context

        var mainClass = CitiesActivity()

        val cities = mainClass.cities

        val codes = mainClass.codes

        val countries = mainClass.countries

        val states = mainClass.states

        init {
            for (i in states.indices) {
                if(states[i] !== "") {
                    states[i] = states[i] + ", "
                }
            }
        }

        override fun getCount(): Int {
            return cities.size
        }

        override fun getItem(position: Int): Any {
            return codes[position] + "&" + cities[position] + ", " + countries[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val cityItem = layoutInflater.inflate(R.layout.city_item, viewGroup, false)

            val title = cityItem.findViewById<TextView>(R.id.title)
            val subtitle = cityItem.findViewById<TextView>(R.id.subtitle)
            title.text = cities[position]
            subtitle.text = states[position] + countries[position]

            return cityItem
        }

    }
}