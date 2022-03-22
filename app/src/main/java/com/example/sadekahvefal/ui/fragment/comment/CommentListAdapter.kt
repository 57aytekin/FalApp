package com.example.sadekahvefal.ui.fragment.comment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sadekahvefal.R
import com.example.sadekahvefal.databinding.CommentRowItemBinding
import com.example.sadekahvefal.databinding.FragmentCommentListBinding
import com.example.sadekahvefal.model.Comment
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.utils.CommentListListener

class CommentListAdapter(
    private val commentListListener: CommentListListener
) : RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Comment,
            newItem: Comment
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitList(list : List<Comment>) = differ.submitList(list)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentListAdapter.CommentViewHolder {
        return CommentViewHolder(
            CommentRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentListAdapter.CommentViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    inner class CommentViewHolder(private val binding: CommentRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(comment : Comment) {
            if (comment.is_showing == 0) binding.cardViewComment.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.purple_500))
            else binding.cardViewComment.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.purple))
            binding.apply {
                tvUserFirstLastName.text = comment.first_name
                tvTopUserName.text = "@"+comment.user_name
                when {
                    comment.diff_date.month > 0 ->  tvCommentDate.text = comment.diff_date.month.toString()+ " ay önce"
                    comment.diff_date.day > 0 ->  tvCommentDate.text = comment.diff_date.day.toString()+ " gün önce"
                    comment.diff_date.hour > 0 -> tvCommentDate.text = comment.diff_date.hour.toString()+ " saat önce"
                    comment.diff_date.minute > 0 -> tvCommentDate.text = comment.diff_date.minute.toString()+ " dakika önce"
                    comment.diff_date.second > 0 -> tvCommentDate.text = "biraz önce"
                }
                if (comment.paths.isNotEmpty()) {
                    Glide.with(binding.root.context).load(comment.paths).into(ivCommentorImage)
                }
                binding.ratingBar.rating = comment.rate.toFloat()
                binding.cardViewComment.setOnClickListener {
                    commentListListener.onItemCLick(comment)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}