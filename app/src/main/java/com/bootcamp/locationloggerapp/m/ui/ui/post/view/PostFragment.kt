package com.bootcamp.locationloggerapp.m.ui.ui.post.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentPostBinding
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.ui.post.viewmodel.PostViewModel
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : Fragment()  {
    private var placeName: String = ""
    private var latitude: Double = 0.0
    private var longitude : Double = 0.0
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    lateinit var selectedPhoto: Uri
    private val openGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                selectedPhoto = it.data?.data!!
                binding.ivPhotoLog.setImageURI(selectedPhoto)
            }
        }
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        args ()
    }

    private fun args (){
        val args = this.arguments
        val nameArgs = args?.get("name")
        val latitudeArgs =  args?.get("latitude")
        val longitudeArgs = args?.get("longitude")
        if (nameArgs !=null){
            binding.tvLocation.text = nameArgs.toString()
            placeName = nameArgs.toString()
            latitude = latitudeArgs as Double
            longitude = longitudeArgs as Double
        }
    }

    private fun initBindings() {
        binding.ivPhotoLog.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            openGallery.launch(intent)
        }

        binding.btnPost.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            postLog()
        }

        binding.tvLocation.setOnClickListener{
            findNavController().navigate(R.id.locationFragment)
        }
    }

    private fun observers(){
        val errorObserver = Observer<String> { errorMessage ->
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireActivity(), "error $errorMessage", Toast.LENGTH_SHORT).show()
        }

        val onCreatedObserver = Observer<Boolean> {
            binding.progressBar.visibility = View.GONE
            findNavController().navigate(R.id.homeFragment)
        }
        viewModel.errorMessage.observe(this, errorObserver)
        viewModel.onPosted.observe(this, onCreatedObserver)
    }

    private fun postLog(){
        val description = binding.tfDescription.text.toString()
        val title = binding.tfLogTitle.text.toString()
        val geoPoint = GeoPoint(latitude, longitude)
        val sharedPreference = requireActivity().getSharedPreferences("currentUserID", Context.MODE_PRIVATE)
        val userId= sharedPreference.getString("userId","")
        val postInfo = Post("",userId,title, description,"",placeName,"",geoPoint)
        viewModel.addPost(postInfo, selectedPhoto)
    }

}