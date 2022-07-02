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
        "Philadelphia", "Detroit", "New York City", "Mumbai"
    )

    val countries = arrayOf(
        "US", "US", "US", "IN"
    )

    val states = arrayOf(
        "PA", "MI", "NY", ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        shared = getSharedPreferences("CityDB" , Context.MODE_PRIVATE)

        val citiesLV = findViewById<ListView>(R.id.citiesList)

        citiesLV.adapter = CustomAdapter(this)

        citiesLV.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val edit = shared.edit()
            edit.putString("city" , selectedItem)
            edit.apply()
            startActivity(Intent(this@CitiesActivity, MainActivity::class.java))
        }
    }

    private class CustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context = context

        var mainClass = CitiesActivity()

        val cities = mainClass.cities

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
            val temp1 = cities[position] + "," + countries[position]
            val temp2 = temp1.filter { !it.isWhitespace() }
            return temp2.lowercase()
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