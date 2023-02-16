package com.bootcamp.locationloggerapp.m.ui.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.locationloggerapp.databinding.ItemLogBinding
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.utils.IHomeList
import com.squareup.picasso.Picasso

class HomeListAdapter(
    private val postDetailList: MutableList<PostDetail>,
    private val listener: IHomeList
) : RecyclerView.Adapter<HomeListAdapter.PersonViewHolder>() {

    class PersonViewHolder(val binding: ItemLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun fillData(postDetail: PostDetail) {
            var url = postDetail.userPhotoUrl
            Picasso.get()
                .load(url)
                .into(binding.ivProfilePicture)
            binding.tvProfileName.text = postDetail.name
            binding.tvTitle.text = postDetail.title
            if (postDetail.placeName != "") {
                binding.tvLocation.text = postDetail.placeName
                binding.tvLocation.visibility = View.VISIBLE
            }
            url = postDetail.photoUrl
            Picasso.get()
                .load(url)
                .into(binding.ivPhotoLog)
            binding.tvDescription.text = postDetail.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLogBinding.inflate(inflater, null, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val post = postDetailList[position]
        holder.fillData(post)

        holder.binding.tvLocation.setOnClickListener {
            listener.locationDetail(post)
        }

        holder.binding.tvDescription.setOnClickListener {
            listener.postDetail(post)
        }

    }

    override fun getItemCount(): Int {
        return postDetailList.size
    }
}