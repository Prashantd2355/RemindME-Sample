package com.pdhameliya.remindme_sample

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.pdhameliya.remindme_sample.adapter.PlaceListAdapter
import com.pdhameliya.remindme_sample.helper.LandmarkResult
import com.pdhameliya.remindme_sample.helper.PlaceResult
import com.pdhameliya.remindme_sample.helper.TypeObject
import com.pdhameliya.remindme_sample.interfaces.PlaceListListner
import com.pdhameliya.remindmelibrary.actions.ReminderStatusCodes
import com.pdhameliya.remindmelibrary.helper.LandmarkSearchParams
import com.pdhameliya.remindmelibrary.landmark.NearbyLandmarks
import com.pdhameliya.remindmelibrary.location.LocationHelper
import com.pdhameliya.remindmelibrary.location.OnReceiveCurrentLocation
import kotlinx.android.synthetic.main.activity_landmarks.*
import java.io.StringReader

class LandmarksActivity : AppCompatActivity(), PlaceListListner,OnReceiveCurrentLocation {

    lateinit var mCurrentLocation:Location

    var landmarkTypeList = arrayOf(
        TypeObject("Airport", "airport", R.drawable.land_plane),
        TypeObject("Atm", "atm", R.drawable.land_atm_machine),
        TypeObject("Amusement Park", "amusement_park", R.drawable.land_amusement_park),
        TypeObject("Bank", "bank", R.drawable.land_bank),
        TypeObject("Bakery", "bakery", R.drawable.land_bakery),
        TypeObject("Bar", "bar", R.drawable.land_bar),
        TypeObject("Beauty Salon", "beauty_salon", R.drawable.land_beuty_slon),
        TypeObject("Bicycle Store", "bicycle_store", R.drawable.land_bycicle),
        TypeObject("Book Store", "book_store", R.drawable.land_book_shop),
        TypeObject("Bus Station", "bus_station", R.drawable.land_bus_stop),
        TypeObject("Cafe", "cafe", R.drawable.land_cafe),
        TypeObject("Car Dealer", "car_dealer", R.drawable.land_car_dealer),
        TypeObject("Car Rental", "car_rental", R.drawable.land_car_rent),
        TypeObject("Car Repair", "car_repair", R.drawable.land_car_repair),
        TypeObject("Car Wash", "car_wash", R.drawable.land_car_wash),
        TypeObject("Casino", "casino", R.drawable.land_casino),
        TypeObject("Cemetery", "cemetery", R.drawable.land_cemetry),
        TypeObject("Church", "church", R.drawable.land_church),
        TypeObject("Convenience Store", "convenience_store", R.drawable.land_convinience_store),
        TypeObject("Dentist", "dentist", R.drawable.land_dentistry),
        TypeObject("Department Store", "department_store", R.drawable.land_departmental_store),
        TypeObject("Doctor", "doctor", R.drawable.land_doctor),
        TypeObject("Embassy", "embassy", R.drawable.land_embassy),
        TypeObject("Funeral Home", "funeral_home", R.drawable.land_funeral_home),
        TypeObject("Furniture Store", "furniture_store", R.drawable.land_furniture_store),
        TypeObject("Gas Station", "gas_station", R.drawable.land_gasstation),
        TypeObject("Gym", "gym", R.drawable.land_gym),
        TypeObject("Hardware Store", "hardware_store", R.drawable.land_hardware),
        TypeObject("Hindu Temple", "hindu_temple", R.drawable.land_hindu_temple),
        TypeObject("Hospital", "hospital", R.drawable.land_hospital),
        TypeObject("Jewelry Store", "jewelry_store", R.drawable.land_jewelry_store),
        TypeObject("Laundry", "laundry", R.drawable.land_laundry),
        TypeObject("Library", "library", R.drawable.land_library),
        TypeObject("Medical Store", "drugstore", R.drawable.land_medical),
        TypeObject("Movie Theater", "movie_theater", R.drawable.land_theater),
        TypeObject("Museum", "museum", R.drawable.land_museum),
        TypeObject("Parking", "parking", R.drawable.land_parking),
        TypeObject("Pharmacy", "pharmacy", R.drawable.land_pharmacy),
        TypeObject("Police", "police", R.drawable.land_police),
        TypeObject("Post Office", "post_office", R.drawable.land_postoffice),
        TypeObject("Restaurant", "restaurant", R.drawable.land_restaurant),
        TypeObject("Shopping Mall", "shopping_mall", R.drawable.land_shopping),
        TypeObject("Supermarket", "supermarket", R.drawable.land_supermarket),
        TypeObject("Taxi Stand", "taxi_stand", R.drawable.land_taxi),
        TypeObject("Train Station", "train_station", R.drawable.land_train_station),
        TypeObject("University", "university", R.drawable.land_university),
        TypeObject("Zoo", "zoo", R.drawable.land_zoo),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.landmarks)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        LocationHelper().getUserCurrentLocation(this,this)
        main()
    }

    private fun main() {
        rcvLandmarks.layoutManager = GridLayoutManager(
            this, 2,
            RecyclerView.VERTICAL,
            false
        )
        rcvLandmarks.adapter = PlaceListAdapter(landmarkTypeList,this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun placeListClickListner(typeObject: TypeObject) {
        NearbyLandmarks().getNearbyLandmarkList(this,
            LandmarkSearchParams(mCurrentLocation,5000,getString(R.string.google_maps_key),typeObject.keyType.toString())){
            if(it.statusCode== ReminderStatusCodes.SUCCESS){
                val gson = Gson()
                val reader = JsonReader(StringReader(it.landmarkJsonObject.toString()))
                reader.setLenient(true)
                LandmarkResult.placeResult = gson.fromJson(reader, PlaceResult::class.java)
                startActivity(Intent(this,NearbyPlaceListActivity::class.java))
            }else{
                runOnUiThread(Runnable {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                })
            }
            Log.i("LandmarkResult", it.landmarkJsonObject.toString())
        }
    }

    override fun onLocationReceived(location: Location?) {
        mCurrentLocation = location!!
    }
}