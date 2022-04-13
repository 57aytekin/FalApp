package com.falApp.sadekahvefal.ui.fragment.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.utils.ClickListeners
import com.falApp.sadekahvefal.utils.Converters
import com.falApp.sadekahvefal.utils.TopUserClickListener

const val ITEM_TYPE_USER = 0
const val ITEM_TYPE_POST = 1

class HomeRecyclerViewAdapter(private val clickListeners: ClickListeners) :
    PagingDataAdapter<HomeRecyclerViewItem.Post, RecyclerView.ViewHolder>(
        SEARCH_COMPARATOR
    ), TopUserClickListener {
    private var topUserList = listOf<HomeRecyclerViewItem.User>()
    private val userAdapter: TopUserAdapter by lazy { TopUserAdapter(this) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_USER -> {
                HomeRecyclerViewHolder.TopUserViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.user_recycler, parent, false)
                )
            }
            ITEM_TYPE_POST -> {
                HomeRecyclerViewHolder.PostViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.post_item_row, parent, false), parent.context
                )
            }
            else -> throw Exception("Hata")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //holder.itemClickListener = itemClickListener
        val item = getItem(position)
        when (holder) {
            is HomeRecyclerViewHolder.PostViewHolder -> {
                val postHolder : HomeRecyclerViewHolder.PostViewHolder = holder
                if (!item?.paths!!.isNullOrEmpty())
                    Glide.with(postHolder.context).load(item.paths).into(postHolder.userImage)
                when {
                    item.diff_date!!.month > 0 ->  holder.tvPostDate.text = item.diff_date.month.toString()+ " ay önce"
                    item.diff_date.day > 0 ->  holder.tvPostDate.text = item.diff_date.day.toString()+ " gün önce"
                    item.diff_date.hour > 0 -> holder.tvPostDate.text = item.diff_date.hour.toString()+ " saat önce"
                    item.diff_date.minute > 0 -> holder.tvPostDate.text = item.diff_date.minute.toString()+ " dakika önce"
                    item.diff_date.second > 0 -> holder.tvPostDate.text = "biraz önce"
                }
                postHolder.userFirstName.text = item.first_name
                postHolder.userName.text = item.user_name
                Glide.with(postHolder.context).load(item.image_1).into(postHolder.image1)
                Glide.with(postHolder.context).load(item.image_2).into(postHolder.image2)
                Glide.with(postHolder.context).load(item.image_3).into(postHolder.image3)
                postHolder.itemView.setOnClickListener { clickListeners.onButtonCLick(item) }
            }
            is HomeRecyclerViewHolder.TopUserViewHolder -> {
                val userHolder : HomeRecyclerViewHolder.TopUserViewHolder = holder
                userHolder.recyclerView.apply {
                    layoutManager = LinearLayoutManager(
                        holder.recyclerView.context,
                        RecyclerView.HORIZONTAL,
                        false
                    )
                    adapter = userAdapter
                }
                userAdapter.submitList(topUserList)
            }
        }
    }

    private fun convertStringToBitmap(path: String) : Bitmap {
        val byteArray = Base64.decode(path, 0)
        return Converters().toBitmap(byteArray)!!
    }


    override fun getItemViewType(position: Int): Int {
        return  if (topUserList.isNotEmpty() && position == 0) ITEM_TYPE_USER
        else ITEM_TYPE_POST
    }

    fun updateUserTopList(list: List<HomeRecyclerViewItem.User>) {
        topUserList = list
    }

    companion object {
        private val SEARCH_COMPARATOR =
            object : DiffUtil.ItemCallback<HomeRecyclerViewItem.Post>() {
                override fun areItemsTheSame(
                    oldItem: HomeRecyclerViewItem.Post,
                    newItem: HomeRecyclerViewItem.Post
                ): Boolean =
                    oldItem.post_id == newItem.post_id

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: HomeRecyclerViewItem.Post,
                    newItem: HomeRecyclerViewItem.Post
                ): Boolean =
                    oldItem == newItem

            }
    }

    override fun onUserClick(userId: Int) {
        clickListeners.onUserClick(userId)
    }
}

