package com.falApp.sadekahvefal.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falApp.sadekahvefal.databinding.ImageRowItemBinding

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitList(list : List<String>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            ImageRowItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
    }

    inner class SliderViewHolder(val binding : ImageRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(url : String) {
            Glide.with(binding.root.context).load(url).into(binding.ivSliderImage)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}