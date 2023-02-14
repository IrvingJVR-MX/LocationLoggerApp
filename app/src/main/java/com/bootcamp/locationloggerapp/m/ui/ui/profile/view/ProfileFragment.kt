package com.bootcamp.locationloggerapp.m.ui.ui.profile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bootcamp.locationloggerapp.R
import com.bootcamp.locationloggerapp.databinding.FragmentProfileBinding
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.ui.LoginActivity
import com.bootcamp.locationloggerapp.m.ui.ui.profile.Adapter.ProfileListAdapter
import com.bootcamp.locationloggerapp.m.ui.ui.profile.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), ProfileListAdapter.IListListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProfileListAdapter
    private var postList = mutableListOf<Post>()
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var user : User
    private var positionList = 0
    private lateinit var builder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        builder = AlertDialog.Builder(requireContext())
        observers()
        initButton()
        val sharedPreference = requireActivity().getSharedPreferences("currentUserID", Context.MODE_PRIVATE)
        val userId= sharedPreference.getString("userId","") as String
        viewModel.getUserInfo(userId)
    }

    private fun initButton(){
        binding.ivSignOut.setOnClickListener{
            viewModel.signOut()
        }
        binding.ivEditProfile.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("userId",user.id)
            bundle.putString("userName",user.name.toString())
            bundle.putString("profilePhotoUrl", user.profilePhotoUrl)
            bundle.putString("profilePhotoPath", user.profilePhotoPath)
            bundle.putString("backgroundPhotoUrl", user.backgroundPhotoUrl)
            bundle.putString("backgroundPhotoPath", user.backgroundPhotoPath)
            findNavController().navigate(R.id.editProfileFragment, bundle)
        }
    }

    private fun observers() {
        val errorObserver = Observer<String> { errorMessage ->
            Toast.makeText(requireActivity(), "error $errorMessage", Toast.LENGTH_SHORT).show()
        }
        val onListFilled = Observer<MutableList<Post>> { list ->
            postList = list
            if (postList.size == 0){
                binding.tvAddPost.visibility = View.VISIBLE
                binding.tvAddPost.text = binding.root.resources.getString(R.string.add_post)
            }else{
                binding.tvAddPost.visibility = View.GONE
            }
            initList()
        }
        val userInfoFilled = Observer<User> { userInfo ->
            val url = userInfo.profilePhotoUrl
            Picasso.get()
                .load(url)
                .into(binding.ivProfilePicture)
            val backgroundUrl = userInfo.backgroundPhotoUrl
            if (backgroundUrl != ""){
                binding.ivCoverPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                Picasso.get()
                    .load(backgroundUrl)
                    .into(binding.ivCoverPhoto)
            }
            user = userInfo
            val id = userInfo.id?.let {  userInfo.id } ?: ""
            viewModel.getUserCollections(id)
        }
        val onUserSignOut = Observer<Boolean> {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        val onDeletedPost = Observer<Boolean> {
           updateList()
        }
        viewModel.errorMessage.observe(requireActivity(), errorObserver)
        viewModel.userInfo.observe(requireActivity(), userInfoFilled)
        viewModel.logPostList.observe(requireActivity(), onListFilled)
        viewModel.onUserSignOut.observe(requireActivity(), onUserSignOut)
        viewModel.onDeletedPost.observe(requireActivity(), onDeletedPost)
    }

    private fun updateList(){
        postList.removeAt(positionList)
        adapter.notifyItemRemoved(positionList)
    }

    private fun initList() {
        adapter = ProfileListAdapter(postList, user, this)
        setRecycler()
    }
    private fun setRecycler() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewPeople.adapter = adapter
        binding.recyclerviewPeople.layoutManager = linearLayoutManager
    }
    override fun locationDetail(post: Post) {
        val bundle = Bundle()
        bundle.putString("destination","profile")
        bundle.putString("placeName",post.placeName)
        bundle.putDouble("latitude", post.geoPoint!!.latitude)
        bundle.putDouble("longitude", post.geoPoint.longitude)
        findNavController().navigate(R.id.mapFragment, bundle)
    }

    override fun deletePost(position: Int) {
        builder.setTitle(binding.root.resources.getString(R.string.caution))
            .setMessage(binding.root.resources.getString(R.string.delete_post))
            .setPositiveButton(binding.root.resources.getString(R.string.yes)){ _, _ ->
                val id = postList[position].id?.let {  postList[position].id } ?: ""
                val path = postList[position].photoPath?.let {  postList[position].photoPath} ?: ""
                viewModel.deletePhoto(id, path)
            }
            .setNegativeButton(binding.root.resources.getString(R.string.no)){ dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }
    override fun postDetail(post : Post) {
        val bundle = Bundle()
        bundle.putString("title",post.title)
        bundle.putString("destination","home")
        bundle.putString("description",post.description)
        bundle.putString("photoUrl",post.photoUrl)
        bundle.putString("placeName",post.placeName)
        bundle.putDouble("latitude", post.geoPoint!!.latitude)
        bundle.putDouble("longitude", post.geoPoint.longitude)
        findNavController().navigate(R.id.detailPostFragment, bundle)
    }

}