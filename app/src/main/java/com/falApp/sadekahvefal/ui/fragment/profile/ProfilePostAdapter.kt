package com.falApp.sadekahvefal.ui.fragment.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falApp.sadekahvefal.databinding.ProfileUserPostItemBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem

class ProfilePostAdapter : RecyclerView.Adapter<ProfilePostAdapter.ProfileViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<HomeRecyclerViewItem.Post>() {
        override fun areItemsTheSame(oldItem: HomeRecyclerViewItem.Post, newItem: HomeRecyclerViewItem.Post): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HomeRecyclerViewItem.Post,
            newItem: HomeRecyclerViewItem.Post
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitList(list : List<HomeRecyclerViewItem.Post?>) = differ.submitList(list)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            ProfileUserPostItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    inner class ProfileViewHolder(private val binding: ProfileUserPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post : HomeRecyclerViewItem.Post) {
            Glide.with(binding.root.context).load(post.image_1).into(binding.ivProfilePostCoffee1)
            Glide.with(binding.root.context).load(post.image_2).into(binding.ivProfilePostCoffee2)
            Glide.with(binding.root.context).load(post.image_3).into(binding.ivProfilePostCoffee3)
        }
    }

    override fun getItemCount() = differ.currentList.size
}