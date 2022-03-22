package com.example.sadekahvefal.ui.fragment.home

import android.annotation.SuppressLint
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sadekahvefal.R
import com.example.sadekahvefal.databinding.TopCommentersItemRowBinding
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.utils.Converters

class TopUserAdapter(): RecyclerView.Adapter<TopUserAdapter.UserHolder>() {
    private var userList : ArrayList<HomeRecyclerViewItem.User> = arrayListOf()

    private val diffCallBack = object : DiffUtil.ItemCallback<HomeRecyclerViewItem.User>() {
        override fun areItemsTheSame(oldItem: HomeRecyclerViewItem.User, newItem: HomeRecyclerViewItem.User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HomeRecyclerViewItem.User,
            newItem: HomeRecyclerViewItem.User
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitList(list : List<HomeRecyclerViewItem.User>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            TopCommentersItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = differ.currentList[position]
        holder.bind(user)
    }

    inner class UserHolder(private val binding: TopCommentersItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(user : HomeRecyclerViewItem.User) = binding.run {
            /*if (user.paths!!.isNotEmpty()) {
                val byteArray = Base64.decode(user.paths, 0)
                val bitmap = Converters().toBitmap(byteArray)!!
                ivUserTopImage.setImageBitmap(bitmap)
            }*/
            tvUserFirstLastName.text = user.first_name + " " + user.last_name
            tvTopUserName.text = "@"+user.user_name
        }
    }

    fun setUserList(userList: ArrayList<HomeRecyclerViewItem.User>) {
        this.userList = userList
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}