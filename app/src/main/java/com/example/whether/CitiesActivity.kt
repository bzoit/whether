package com.example.whether

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CitiesActivity : AppCompatActivity() {

    val cities = arrayOf(
        "Philadelphia", "Detroit", "New York City", "Mumbai"
    )

    val countries = arrayOf(
        "USA", "USA", "USA", "India"
    )

    val states = arrayOf(
        "PA", "MI", "NY", ""
    )

    val codes = arrayOf(
        "9018", "9712", "2765", "7839"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        val citiesLV = findViewById<ListView>(R.id.citiesList)

        citiesLV.adapter = CustomAdapter(this)

        citiesLV.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            println(selectedItem)
        }
    }

    private class CustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context = context

        var mainClass = CitiesActivity()

        val cities = mainClass.cities

        val countries = mainClass.countries

        val states = mainClass.states

        val codes = mainClass.codes

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
            return codes[position]
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