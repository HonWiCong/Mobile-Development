package com.example.assignment6

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class ViewMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var locationText : TextView
    private lateinit var floatingActionButton : FloatingActionButton
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    
    var latitude = 0.00
    var longitude = 0.00
    val key = "65afe04d18ae4fa8aeed2ade0ca9f6e3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_map, container, false)
        locationText = view.findViewById(R.id.location_text)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        mapView = view.findViewById(R.id.mapView)

        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        cameraButton()
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        latitude = 1.5083926480220609
        longitude = 110.32589079583315

        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title("testing"))

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))

        googleMap.setOnMarkerClickListener {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(it.position.latitude, it.position.longitude, 1)

            if (addresses?.isNotEmpty() == true) {
                val address = addresses[0].getAddressLine(0)
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                locationText.text = "$address, $city, $state, $country"
            }

            true
        }
    }

    private fun cameraButton() {
        floatingActionButton.setOnClickListener {
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            startActivity(intent)

            geocodingApi()
            getLocation()
        }
    }

    private fun geocodingApi() {
        GlobalScope.launch(Dispatchers.Main) {
            val url = "https://api.geoapify.com/v1/geocode/reverse?lat=$latitude&lon=$longitude&format=json&apiKey=$key"
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
//                    locationText.text = response.toString()
            },
                { error ->
                    Log.d("vol", error.toString())
                })

            Volley.newRequestQueue(context).add(jsonObjectRequest)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val list = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    locationText.text = "${list?.get(0)?.latitude} ${list?.get(0)?.longitude}"
                }
            }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}