package com.bootcamp.locationloggerapp.m.ui.ui.profile.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.locationloggerapp.databinding.ItemLogProfileBinding
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.squareup.picasso.Picasso

class ProfileListAdapter(private val postDetailList: MutableList<Post>,
                         private val user : User,
                         private val listener: IListListener
) : RecyclerView.Adapter<ProfileListAdapter.PersonViewHolder> () {
    interface IListListener {
        fun locationDetail(post: Post)
        fun deletePost(position: Int)
        fun postDetail(post: Post)
    }
    class PersonViewHolder(val binding: ItemLogProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun fillData(post: Post, user: User){
            var url = user.profilePhotoUrl
            Picasso.get()
                .load(url)
                .into(binding.ivProfilePicture)
            binding.tvProfileName.text = user.name
            binding.tvTitle.text = post.title
            if (post.placeName != ""){
                binding.tvLocation.text  = post.placeName
                binding.tvLocation.visibility = View.VISIBLE
            }
            url = post.photoUrl
            Picasso.get()
                .load(url)
                .into(binding.ivPhotoLog)
            binding.tvDescription.text = post.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLogProfileBinding.inflate(inflater, null, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val post = postDetailList[position]
        holder.fillData(post, user)
        holder.binding.tvLocation.setOnClickListener{
            listener.locationDetail(post)
        }
        holder.binding.ivDeletePost.setOnClickListener{
            listener.deletePost(position)
        }

        holder.binding.tvDescription.setOnClickListener {
            listener.postDetail(post)
        }

    }

    override fun getItemCount(): Int {
        return postDetailList.size
    }
}