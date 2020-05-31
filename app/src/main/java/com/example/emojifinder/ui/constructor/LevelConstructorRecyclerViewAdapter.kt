package com.example.emojifinder.ui.constructor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.databinding.EmojiConstructorItemBinding
import kotlinx.android.synthetic.main.emoji_game_item.view.*

class LevelConstructorRecyclerViewAdapter(private val onClickListener : OnEmojiClickListener) : ListAdapter<EmojiShopModel,
        LevelConstructorRecyclerViewAdapter.KeyboardViewHolder>(
    DiffCallback
) {

    companion object DiffCallback: DiffUtil.ItemCallback<EmojiShopModel>()     {

        override fun areItemsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem.unicode == newItem.unicode
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : KeyboardViewHolder {
        return KeyboardViewHolder(
            EmojiConstructorItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: KeyboardViewHolder, position: Int){
        val emoji = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.bind(emoji)
    }

    class KeyboardViewHolder(private val binding: EmojiConstructorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            binding.emojiConstructorBtn.text = emojiShop?.unicode
            binding.executePendingBindings()
        }
    }

    class OnEmojiClickListener(val clickListener: (emojiShop: EmojiShopModel?) -> Unit) {
        fun onClick(emojiShop: EmojiShopModel) = clickListener(emojiShop)
    }
}