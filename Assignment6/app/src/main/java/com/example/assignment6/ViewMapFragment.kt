package com.example.assignment6

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var locationText : TextView
    private lateinit var floatingActionButton : FloatingActionButton
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationDB : LocationDatabase
    private lateinit var locationImage : ImageView
    private lateinit var googleMap: GoogleMap

    var latitude = 0.00
    var longitude = 0.00
    val key = "65afe04d18ae4fa8aeed2ade0ca9f6e3"

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_map, container, false)
        locationDB = LocationDatabase.getDatabase(requireContext())

        locationText = view.findViewById(R.id.location_text)
        locationImage = view.findViewById(R.id.location_image)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        mapView = view.findViewById(R.id.mapView)

        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)

        floatingActionButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        getLocation()
        this.googleMap = googleMap
        lifecycleScope.launch {
            val locations = readLocation()
            val uniqueLocations = locations.distinctBy { Pair(it.latitude, it.longitude) }

            if (uniqueLocations.isNotEmpty()) {
                for (location in uniqueLocations) {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(location.latitude, location.longitude))
                            .title(location.address)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
                }


                googleMap.setOnMarkerClickListener { marker ->
                    for (location in locations) {
                        if (location.address == marker.title) {
                            locationText.text = location.address
                            displayImage(location.image)
                        }
                    }

                    true
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imagePath = saveImageToStorage(imageBitmap)

//            getLocation()
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                try {
                    val response = geocodingApi()

                    val json = response.getJSONArray("results")
                    val name = json.getJSONObject(0).getString("name")
                    Log.d("Name", name)
                    Log.d("Latitude", latitude.toString())
                    val housenumber = json.getJSONObject(0).getString("housenumber")
                    val street = json.getJSONObject(0).getString("street")
                    val country = json.getJSONObject(0).getString("country")

                    locationText.text = "$latitude, $longitude $name"


                    Log.d("location.latitude", latitude.toString())
                    Log.d("location.longitude", longitude.toString())
                    Log.d("location.address", "$latitude, $longitude $name")
                    Log.d("location.image", imagePath)
                    val location = Location(
                        null,
                        latitude,
                        longitude,
                        "$name, $housenumber, $street, $country",
                        imagePath
                    )
                    Log.d("location.latitude", location.latitude.toString())
                    Log.d("location.longitude", location.longitude.toString())
                    Log.d("location.address", location.address)
                    Log.d("location.image", location.image)

                    writeLocation(location)

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(location.latitude, location.longitude))
                            .title(location.address)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))

                } catch (e: Exception) {
                    Log.d("vol", e.toString())
                }
            }
        }
    }


    private fun saveImageToStorage(bitmap: Bitmap): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_$timeStamp.jpg"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(storageDir, imageFileName)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return imageFile.absolutePath
    }

    private fun displayImage(imagePath: String) {
        val imageFile = File(imagePath)
        if (imageFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            locationImage.setImageBitmap(bitmap)
        }
    }

    private suspend fun geocodingApi(): JSONObject {
        val url = "https://api.geoapify.com/v1/geocode/reverse?lat=${this.latitude}&lon=${this.longitude}&format=json&apiKey=$key"
        Log.d("URL", url)
        return withContext(Dispatchers.IO) {
            val queue = Volley.newRequestQueue(requireContext())
            val responseDeferred = CompletableDeferred<JSONObject>()

            val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    CoroutineScope(Dispatchers.Main).launch {
                        responseDeferred.complete(response)
                    }
                },
                { error ->
                    CoroutineScope(Dispatchers.Main).launch {
                        responseDeferred.completeExceptionally(error)
                    }
                }
            )

            queue.add(request)
            responseDeferred.await()
        }
    }


    private suspend fun writeLocation(location: Location) {
        withContext(Dispatchers.IO) {
            locationDB.locationDao().insert(location)
        }
    }

    private suspend fun readLocation(): ArrayList<Location> {
        return withContext(Dispatchers.IO) {

            ArrayList(locationDB.locationDao().getAll())
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
                    this.latitude = it.latitude
                    Log.d("Returned latitude", latitude.toString())
                    this.longitude = it.longitude
                    Log.d("Returned longitude", longitude.toString())
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