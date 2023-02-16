package com.bootcamp.locationloggerapp.m.ui.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentMapBinding
import com.bootcamp.locationloggerapp.m.ui.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var mMap: GoogleMap? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var placeName: String = ""
    private var destination: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args()
        initButton()
        createFragment()
    }

    private fun initButton() {
        binding.btnBack.setOnClickListener {
            navBack()
        }
    }

    private fun navBack() {
        when (destination) {
            Constants.home -> findNavController().navigate(R.id.homeFragment)
            Constants.profile -> findNavController().navigate(R.id.profileFragment)
            else -> {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    private fun args() {
        val args = this.arguments
        val destinationArgs = args?.get(Constants.destination)
        val placeNameArgs = args?.get(Constants.placeName)
        val latitudeArgs = args?.get(Constants.latitude)
        val longitudeArgs = args?.get(Constants.longitude)
        if (placeNameArgs != null) {
            placeName = placeNameArgs as String
            latitude = latitudeArgs as Double
            longitude = longitudeArgs as Double
            destination = destinationArgs as String
        }
    }

    private fun createFragment() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_route_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addMarker()
    }

    private fun addMarker() {
        mMap!!.addMarker(
            MarkerOptions()
                .title(placeName)
                .position(LatLng(latitude, longitude))
        )
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 18f))

    }

}

