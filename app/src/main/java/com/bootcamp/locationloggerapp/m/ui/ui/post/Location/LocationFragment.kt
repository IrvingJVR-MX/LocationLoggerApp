package com.bootcamp.locationloggerapp.m.ui.ui.post.Location

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentLocationBinding
import com.bootcamp.locationloggerapp.m.ui.utils.Constants
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class LocationFragment : Fragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlacesAutocomplete()
        initButton()
    }

    private fun initButton() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.postFragment)
        }
    }

    private fun initPlacesAutocomplete() {
        val ai: ApplicationInfo = requireContext().packageManager.getApplicationInfo(
            requireContext().packageName,
            PackageManager.GET_META_DATA
        )
        val value = ai.metaData["api_key"]
        val apiKey = value.toString()
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        val autocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        val hint = binding.root.context.getString(R.string.select_location)
        autocompleteSupportFragment?.setHint(hint)
        autocompleteSupportFragment?.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
            )
        )
        autocompleteSupportFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val name = place.name?.let { place.name } ?: ""
                val lang = place.latLng
                val latitude = lang?.latitude?.let { lang.latitude } ?: 0.0
                val longitude = lang?.longitude?.let { lang.longitude } ?: 0.0
                navBack(name, latitude, longitude)
            }

            override fun onError(status: Status) {
                val error = binding.root.context.getString(R.string.unknown_error)
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navBack(name: String, latitude: Double, longitude: Double) {
        if (name == "") {
            findNavController().navigate(R.id.postFragment)
        } else {
            val bundle = Bundle()
            bundle.putString(Constants.name, name)
            bundle.putDouble(Constants.latitude, latitude)
            bundle.putDouble(Constants.longitude, longitude)
            findNavController().navigate(R.id.postFragment, bundle)
        }
    }
}