package com.hamzaazman.kotlinfreetoplay.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hamzaazman.kotlinfreetoplay.R
import com.hamzaazman.kotlinfreetoplay.data.dto.Screenshot
import com.hamzaazman.kotlinfreetoplay.databinding.ReviewRowItemBinding


class ReviewAdapter : ListAdapter<Screenshot, ReviewAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ReviewRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ReviewRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(screenshot: Screenshot) = with(binding) {
            reviewImageView.load(screenshot.image) {
                crossfade(true)
                placeholder(R.drawable.game_placeholder)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Screenshot>() {
        override fun areItemsTheSame(oldItem: Screenshot, newItem: Screenshot) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Screenshot, newItem: Screenshot) =
            oldItem == newItem
    }
}