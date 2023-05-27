package com.hamzaazman.kotlinfreetoplay.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hamzaazman.kotlinfreetoplay.R
import com.hamzaazman.kotlinfreetoplay.databinding.GameRowItemBinding
import com.hamzaazman.kotlinfreetoplay.domain.model.GameUi

class HomeAdapter(
        private val onItemClick: (item: GameUi) -> Unit
) : ListAdapter<GameUi, HomeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
                GameRowItemBinding.inflate(
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

    inner class ViewHolder(private val binding: GameRowItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(gameUi: GameUi) = with(binding) {
            gameTitle.text = gameUi.title
            gameDescription.text = gameUi.short_description
            gameGenre.text = gameUi.genre

            Glide.with(gameImage.context)
                    .load(gameUi.thumbnail)
                    .placeholder(R.drawable.game_placeholder)
                    .into(gameImage)

            if (gameUi.platform.contains("Windows")) {
                gamePlatform.setImageResource(R.drawable.windows)
            }

            if (gameUi.platform.contains("Browser")) {
                gamePlatform.setImageResource(R.drawable.browser)
            }
            binding.root.setOnClickListener { onItemClick(gameUi) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GameUi>() {
        override fun areItemsTheSame(oldItem: GameUi, newItem: GameUi) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GameUi, newItem: GameUi) =
                oldItem == newItem
    }
}