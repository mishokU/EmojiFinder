package com.example.emojifinder.ui.boxes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.databinding.ChestItemBinding
import com.example.emojifinder.ui.account.UserLevelRecyclerViewAdapter
import com.example.emojifinder.ui.shop.EmojiShopModel

class LootBoxRecyclerViewAdapter(private val onClickListener: OnShopItemClickListener) : ListAdapter<EmojiShopModel,
            LootBoxRecyclerViewAdapter.ShopEmojiViewHolder>(
        DiffCallback) {


    companion object DiffCallback: DiffUtil.ItemCallback<EmojiShopModel>()     {

        override fun areItemsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val CALLBACK = object : DiffUtil.ItemCallback<EmojiShopModel>() {
        override fun areItemsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem.text == newItem.text
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ShopEmojiViewHolder {
        return ShopEmojiViewHolder(
            ChestItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ShopEmojiViewHolder, position: Int){
        val emoji = currentList[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.bind(emoji)
    }

    class ShopEmojiViewHolder(private val binding: ChestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            binding.chestEmoji.setText(emojiShop?.text)
            binding.executePendingBindings()
        }
    }

    class OnShopItemClickListener(val clickListener: (track: EmojiShopModel?) -> Unit) {
        fun onClick(track: EmojiShopModel) = clickListener(track)
    }


}