package com.example.emojifinder.ui.game.arcade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.databinding.EmojiGameItemBinding
import com.example.emojifinder.ui.shop.EmojiShopModel
import kotlinx.android.synthetic.main.emoji_game_item.view.*


class GameRecyclerViewAdapter(private val onClickListener: OnEmojiClickListener) :
    ListAdapter<EmojiShopModel,
            GameRecyclerViewAdapter.GameViewHolder>(
        DiffCallback
    ) {

    companion object DiffCallback : DiffUtil.ItemCallback<EmojiShopModel>() {

        override fun areItemsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            EmojiGameItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val emoji = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.itemView.emoji_game_view.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.bind(emoji)
    }

    class GameViewHolder(private val binding: EmojiGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            binding.emojiGameView.text = emojiShop?.text
            binding.executePendingBindings()
        }
    }

    class OnEmojiClickListener(val clickListener: (emojiShop: EmojiShopModel?) -> Unit) {
        fun onClick(emojiShop: EmojiShopModel) = clickListener(emojiShop)
    }
}