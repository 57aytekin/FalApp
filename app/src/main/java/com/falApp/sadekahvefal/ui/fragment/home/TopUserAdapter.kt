package com.falApp.sadekahvefal.ui.fragment.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falApp.sadekahvefal.databinding.TopCommentersItemRowBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.utils.ClickListeners
import com.falApp.sadekahvefal.utils.TopUserClickListener

class TopUserAdapter(
    private val topUserClickListener: TopUserClickListener
): RecyclerView.Adapter<TopUserAdapter.UserHolder>() {
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
        holder.bind(user, holder)
        holder.itemView.setOnClickListener {
            topUserClickListener.onUserClick(user.user_id!!)
        }
    }

    inner class UserHolder(private val binding: TopCommentersItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(user : HomeRecyclerViewItem.User, holder : UserHolder) = binding.run {
            if (!user.paths!!.isNullOrEmpty())
                Glide.with(holder.itemView.context).load(user.paths).into(ivUserTopImage)
            tvUserFirstLastName.text = user.first_name + " " + user.last_name
            tvTopUserName.text = "@"+user.user_name
            tvTopUserScore.text = user.score.toString()
        }
    }

    fun setUserList(userList: ArrayList<HomeRecyclerViewItem.User>) {
        this.userList = userList
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}