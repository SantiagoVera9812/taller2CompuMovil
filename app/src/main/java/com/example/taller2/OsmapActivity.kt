package com.example.taller2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taller2.databinding.ActivityOsmapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


class OsmapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOsmapBinding
    lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOsmapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(this,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(this))
        map = binding.osmMap
        map.setTileSource(
            TileSourceFactory.
        MAPNIK)
        map.setMultiTouchControls(true)


    }
    val latitude = 4.62
    val longitude = -74.07
    val startPoint = GeoPoint(latitude, longitude)

    override fun onResume() {
        super.onResume()
        map.onResume()
        map.
        controller.setZoom(18.0)
        map.
        controller.animateTo(startPoint)
    }
    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}