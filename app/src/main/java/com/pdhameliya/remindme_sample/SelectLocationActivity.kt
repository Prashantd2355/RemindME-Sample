package com.pdhameliya.remindme_sample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pdhameliya.remindmelibrary.helper.LocationUIHelper
import com.pdhameliya.remindmelibrary.helper.PermissionCheck
import kotlinx.android.synthetic.main.activity_select_location.*


class SelectLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val PERMISSION_ID: Int = 2021
    private val REQUEST_CHECK_SETTINGS: Int = 2022
    private lateinit var client: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng
    private lateinit var userCurrentLocation: Location
    private lateinit var mMap: GoogleMap

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.title_select_location)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        MapsInitializer.initialize(this)
        // on FusedLocationClient
        client = LocationServices.getFusedLocationProviderClient(this)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        saveLocation.setOnClickListener {
            val intent = Intent()
            intent.putExtra("lattitude", currentLatLng.latitude)
            intent.putExtra("longitude", currentLatLng.longitude)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    private fun getLastLocation() {
        if (checkPermissions()) {
            /**
             * check if location is enabled
             */
            if (isLocationEnabled()) {
                /**
                 * getting last location from FusedLocationClient object
                 */
                if (!PermissionCheck.checkLocationPermission(this)) {
                    PermissionCheck.requestLocationPermission(this)
                }
                client.lastLocation
                    .addOnCompleteListener { task ->
                        try {
                            val location: Location = task.result!!
                            if (location == null) {
                                requestNewLocationData()
                            } else {
                                userCurrentLocation = location
                                currentLatLng = LatLng(location.latitude, location.longitude)
                                initializeMap()
                            }
                        }catch (err:Exception){
                            requestNewLocationData()
                        }
                    }
            } else {
                LocationUIHelper.buildAlertMessageNoGps(this,REQUEST_CHECK_SETTINGS)
            }
        } else {
            /**
             * if permissions aren't available, request for permissions
             */
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initializeMap() {
        Log.i("currentLatLng",currentLatLng.toString())
        val marker = mMap.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .title(getString(R.string.long_press_marker))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        // Enable GPS marker in Map
        mMap.isMyLocationEnabled = true
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
        mMap.animateCamera(cameraUpdate)
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 1000, null)
        mMap.setOnCameraMoveListener {
            val midLatLng = mMap.cameraPosition.target
            if (marker != null) {
                marker.position = midLatLng
                currentLatLng = marker.position
                Log.i("currentLatLng",currentLatLng.toString())
            }
        }
    }

    // initializing fusedAPI callback
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.getLastLocation()
            userCurrentLocation = mLastLocation!!
            currentLatLng = LatLng(userCurrentLocation.latitude,userCurrentLocation.longitude)
            initializeMap()
        }
    }

    /**
     * if now last location is found request new coordinates
     */
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        mLocationRequest.setInterval(5)
        mLocationRequest.setFastestInterval(0)
        mLocationRequest.setNumUpdates(1)
        // setting LocationRequest
        client.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    /**
     * Method to check for permissions
     */
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Method to request for permissions
     */
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    /**
     * Method to check if location is enabled
     * @return true || false
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * GPS permission on ActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK ->                     // All required changes were successfully made
                    getLastLocation()
                RESULT_CANCELED ->                     // The user was asked to change settings, but chose not to
                    Toast.makeText(this,resources.getString(R.string.gps_denied),Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLastLocation() //your lat lng
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}