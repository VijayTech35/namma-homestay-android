package com.nammahomestay.ui.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nammahomestay.databinding.ActivityLocationPickerBinding

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityLocationPickerBinding
    private var googleMap: GoogleMap? = null
    private var selectedLat = 0.0
    private var selectedLng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedLat = intent.getDoubleExtra("lat", 0.0)
        selectedLng = intent.getDoubleExtra("lng", 0.0)

        val mapFragment = supportFragmentManager
            .findFragmentById(com.nammahomestay.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnConfirm.setOnClickListener {
            val data = Intent().apply {
                putExtra("lat", selectedLat)
                putExtra("lng", selectedLng)
            }
            setResult(RESULT_OK, data)
            finish()
        }

        binding.btnCurrentLocation.setOnClickListener {
            googleMap?.let { map ->
                map.clear()
                map.uiSettings.isMyLocationButtonEnabled = true
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMapToolbarEnabled = true

        if (selectedLat != 0.0 && selectedLng != 0.0) {
            val latLng = LatLng(selectedLat, selectedLng)
            map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(13.0, 77.5), 5f))
        }

        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            selectedLat = latLng.latitude
            selectedLng = latLng.longitude
            binding.tvCoordinates.text = "${String.format("%.4f", selectedLat)}, ${String.format("%.4f", selectedLng)}"
        }
    }
}
