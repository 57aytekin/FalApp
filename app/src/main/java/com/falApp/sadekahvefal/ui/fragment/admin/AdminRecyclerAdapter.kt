package com.falApp.sadekahvefal.ui.fragment.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.databinding.AdminRowItemBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.utils.AdminItemClickListener
import com.falApp.sadekahvefal.utils.Commentator

class AdminRecyclerAdapter(private val adminItemClickListener: AdminItemClickListener) : RecyclerView.Adapter<AdminRecyclerAdapter.AdminViewHolder>()  {
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
    fun submitList(list : List<HomeRecyclerViewItem.Post>) = differ.submitList(list)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminRecyclerAdapter.AdminViewHolder {
        return AdminViewHolder(
            AdminRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: AdminRecyclerAdapter.AdminViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    inner class AdminViewHolder(private val binding: AdminRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: HomeRecyclerViewItem.Post) {
            if (item.commentator == Commentator.Falci.ordinal) {
                binding.btnCheck.setImageResource(R.drawable.ic_baseline_send_24)
            }
            if (!item.paths!!.isNullOrEmpty()){}
                Glide.with(binding.root.context).load(item.paths).into(binding.ivUserImage)
            when {
                item.diff_date!!.month > 0 ->  binding.tvPostDate.text = item.diff_date.month.toString()+ " ay önce"
                item.diff_date.day > 0 ->  binding.tvPostDate.text = item.diff_date.day.toString()+ " gün önce"
                item.diff_date.hour > 0 -> binding.tvPostDate.text = item.diff_date.hour.toString()+ " saat önce"
                item.diff_date.minute > 0 -> binding.tvPostDate.text = item.diff_date.minute.toString()+ " dakika önce"
                item.diff_date.second > 0 -> binding.tvPostDate.text = "biraz önce"
            }
            binding.tvUserFirstName.text = item.first_name
            binding.tvUserName.text = item.user_name
            Glide.with(binding.root.context).load(item.image_1).into(binding.ivPostCoffee1)
            Glide.with(binding.root.context).load(item.image_2).into(binding.ivPostCoffee2)
            Glide.with(binding.root.context).load(item.image_3).into(binding.ivPostCoffee3)
            binding.btnCheck.setOnClickListener {
                adminItemClickListener.isConfirmItem(item, 1, item.token!!, item.commentator)
            }
            binding.btnUnCheck.setOnClickListener {
                adminItemClickListener.isConfirmItem(item, 0, item.token!!, null)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size
}