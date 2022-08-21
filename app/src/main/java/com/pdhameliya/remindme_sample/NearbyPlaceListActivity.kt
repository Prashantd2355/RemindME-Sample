package com.pdhameliya.remindme_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdhameliya.remindme_sample.adapter.NearbyPlaceAdapter
import com.pdhameliya.remindme_sample.helper.LandmarkResult
import kotlinx.android.synthetic.main.activity_nearby_place_list.*

class NearbyPlaceListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_place_list)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.place_list)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        main()
    }

    private fun main() {
        rcvPlaceResult.layoutManager = LinearLayoutManager(this)
        val result = LandmarkResult.placeResult?.results
        rcvPlaceResult.adapter = NearbyPlaceAdapter(result!!,this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}