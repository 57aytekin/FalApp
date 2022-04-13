package com.falApp.sadekahvefal.ui.fragment.home

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem

sealed class HomeRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    var itemClickListener: ((view: View, item : HomeRecyclerViewItem, position : Int) -> Unit)? = null

    class TopUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val recyclerView : RecyclerView = itemView.findViewById(R.id.rvTopUser)
    }

    class PostViewHolder(itemView: View, cntx : Context) :
        RecyclerView.ViewHolder(itemView) {
        val context = cntx
        val userImage: ImageView = itemView.findViewById(R.id.ivUserImage)
        val userFirstName: TextView = itemView.findViewById(R.id.tvUserFirstName)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvPostDate: TextView = itemView.findViewById(R.id.tvPostDate)

        val image1: ImageView = itemView.findViewById(R.id.ivPostCoffee1)
        val image2: ImageView = itemView.findViewById(R.id.ivPostCoffee2)
        val image3: ImageView = itemView.findViewById(R.id.ivPostCoffee3)
    }
}
