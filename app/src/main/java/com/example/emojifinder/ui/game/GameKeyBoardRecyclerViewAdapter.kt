package com.example.emojifinder.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.data.db.remote.models.EmojiModel
import com.example.emojifinder.databinding.CategoryCellItemBinding
import com.example.emojifinder.databinding.EmojiGameItemBinding
import com.example.emojifinder.databinding.EmojiShopItemBinding
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.shop.EmojiShopModel
import kotlinx.android.synthetic.main.emoji_game_item.view.*


class GameKeyBoardRecyclerViewAdapter(private val onClickListener : OnEmojiClickListener) : ListAdapter<EmojiModel,
        GameKeyBoardRecyclerViewAdapter.KeyboardViewHolder>(
    DiffCallback
) {

    companion object DiffCallback: DiffUtil.ItemCallback<EmojiModel>()     {

        override fun areItemsTheSame(oldItem: EmojiModel, newItem: EmojiModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EmojiModel, newItem: EmojiModel): Boolean {
            return oldItem.unicode == newItem.unicode
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : KeyboardViewHolder {
        return KeyboardViewHolder(
            EmojiGameItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: KeyboardViewHolder, position: Int){
        val emoji = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.itemView.emoji_game_view.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.bind(emoji)
    }

    class KeyboardViewHolder(private val binding: EmojiGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emoji: EmojiModel?) {
            binding.emojiGameView.text = emoji?.unicode
            binding.executePendingBindings()
        }
    }

    class OnEmojiClickListener(val clickListener: (emoji: EmojiModel?) -> Unit) {
        fun onClick(emoji: EmojiModel) = clickListener(emoji)
    }
}