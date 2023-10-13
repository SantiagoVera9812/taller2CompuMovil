package com.example.taller2

import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.taller2.databinding.ActivityGalleryBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private lateinit var localClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location
    val getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission(), ActivityResultCallback { updateUI() })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationRequest = createLocationRequest()
        locationCallback = createLocationCallback()

    }

    private fun createLocationCallback(): LocationCallback {

        val locationCallback = object : LocationCallback(){

            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                if(p0 != null ){

                    val location = p0.lastLocation!!
                    updateUI()
                }
            }
        }
        return locationCallback
    }

    private fun updateUI() {



        if (lastLocation != null) {


            binding.altitud.text = lastLocation.altitude.toString()
            binding.latitud.text = lastLocation.longitude.toString()
            binding.longitud.text = lastLocation.latitude.toString()
            Log.i("LocationT", "altitud: ${lastLocation.altitude}")
            Log.i("LocationK", "latitud ${lastLocation.latitude}")
            Log.i("Location", "Longitud: ${lastLocation.longitude}")
        }

    }

    fun createLocationRequest():LocationRequest{

        locationRequest = LocationRequest.create()
            .setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        return locationRequest
    }

    val locationSettings: ActivityResultLauncher<IntentSenderRequest> = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            startLocationUpdates()
        } else {
            binding.altitud.text = "Apagado"
        }
    }

    fun locationSettings(){
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->

            startLocationUpdates()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){

                try {
                    val isr : IntentSenderRequest = IntentSenderRequest.Builder(exception.
                    resolution).build()
                    locationSettings.launch(isr)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }

        fun checkLocationPermission(){

        if((checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_DENIED)
            if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)){
                binding.latitud.text = "se requierte gps para"
            }

        getPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

        }

    private fun startLocationUpdates() {
        if((checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED){
            val looper = Looper.getMainLooper()
            localClient.requestLocationUpdates(locationRequest,locationCallback, looper)
    }}}
