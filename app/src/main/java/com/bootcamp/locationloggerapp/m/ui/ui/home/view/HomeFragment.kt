package com.bootcamp.locationloggerapp.m.ui.ui.home.view


import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentHomeBinding
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.ui.home.viewmodel.HomeViewModel
import com.bootcamp.locationloggerapp.m.ui.ui.home.adapter.HomeListAdapter
import com.bootcamp.locationloggerapp.m.ui.utils.Constants
import com.bootcamp.locationloggerapp.m.ui.utils.IHomeList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), IHomeList {
    private lateinit var adapter: HomeListAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userList = mutableListOf<PostDetail>()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        viewModel.getUser()
    }

    private fun observers() {
        val errorObserver = Observer<String> { errorMessage ->
            Toast.makeText(requireActivity(), "error $errorMessage", Toast.LENGTH_SHORT).show()
        }
        val onListFilled = Observer<MutableList<PostDetail>> { list ->
            userList = list
            initList()
        }
        viewModel.errorMessage.observe(requireActivity(), errorObserver)
        viewModel.logPostList.observe(requireActivity(), onListFilled)
    }

    private fun initList() {
        adapter = HomeListAdapter(userList, this)
        setRecycler()
    }


    private fun setRecycler() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewPeople.adapter = adapter
        binding.recyclerviewPeople.layoutManager = linearLayoutManager
    }

    override fun locationDetail(post: PostDetail) {
        val bundle = Bundle()
        bundle.putString(Constants.destination, Constants.home)
        bundle.putString(Constants.placeName, post.placeName)
        bundle.putDouble(Constants.latitude, post.geoPoint!!.latitude)
        bundle.putDouble(Constants.longitude, post.geoPoint.longitude)
        findNavController().navigate(R.id.mapFragment, bundle)
    }

    override fun postDetail(post: PostDetail) {
        val bundle = Bundle()
        bundle.putString(Constants.title, post.title)
        bundle.putString(Constants.destination, Constants.home)
        bundle.putString(Constants.description, post.description)
        bundle.putString(Constants.photoUrl, post.photoUrl)
        bundle.putString(Constants.placeName, post.placeName)
        bundle.putDouble(Constants.latitude, post.geoPoint!!.latitude)
        bundle.putDouble(Constants.longitude, post.geoPoint.longitude)
        findNavController().navigate(R.id.detailPostFragment, bundle)
    }


}

