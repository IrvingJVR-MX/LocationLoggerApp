package com.bootcamp.locationloggerapp.m.ui.ui.detailpost.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentDetailPostBinding
import com.bootcamp.locationloggerapp.m.ui.utils.Constants
import com.squareup.picasso.Picasso

class DetailPostFragment : Fragment() {
    private var _binding: FragmentDetailPostBinding? = null
    private val binding get() = _binding!!
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var placeName: String = ""
    private var destination: String = ""
    private var title: String = ""
    private var description: String = ""
    private var photoUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args()
        initPost()
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

    private fun initPost() {
        binding.tvTitle.text = title
        binding.tvLocation.text = placeName
        val url = photoUrl
        Picasso.get()
            .load(url)
            .into(binding.ivPost)
        binding.tvDescription.text = description

    }

    private fun args() {
        val args = this.arguments
        val placeNameArgs = args?.get(Constants.placeName)
        val latitudeArgs = args?.get(Constants.latitude)
        val longitudeArgs = args?.get(Constants.longitude)
        val titleArgs = args?.get(Constants.title)
        val descriptionArgs = args?.get(Constants.description)
        val photoUrlArgs = args?.get(Constants.photoUrl)
        val destinationArgs = args?.get(Constants.destination)
        if (placeNameArgs != null) {
            placeName = placeNameArgs as String
            latitude = latitudeArgs as Double
            longitude = longitudeArgs as Double
            title = titleArgs as String
            description = descriptionArgs as String
            photoUrl = photoUrlArgs as String
            destination = destinationArgs as String
        }
    }

}