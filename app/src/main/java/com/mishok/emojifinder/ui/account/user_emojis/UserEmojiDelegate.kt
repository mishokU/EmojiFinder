package com.mishok.emojifinder.ui.account.user_emojis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.mishok.emojifinder.R
import com.mishok.emojifinder.databinding.EmojiUserItemBinding
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import kotlinx.android.synthetic.main.emoji_shop_item.view.*

class UserEmojiDelegate(
    private val emoji: String,
    private val callback: (EmojiShopModel) -> Unit
) : AbsListItemAdapterDelegate<EmojiShopModel, EmojiShopModel, UserEmojiDelegate.Holder>() {

    override fun isForViewType(item: EmojiShopModel, items: MutableList<EmojiShopModel>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(EmojiUserItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(item: EmojiShopModel, holder: Holder, payloads: MutableList<Any>) {
        if(holder.itemView.emoji_view.backgroundTintList ==
            holder.itemView.resources.getColorStateList(R.color.green_color)) {
            holder.setIsRecyclable(false)
        }
        holder.itemView.setOnClickListener {
            callback(item)
        }
        holder.itemView.emoji_view.setOnClickListener {
            callback(item)
        }
        holder.bind(item)
    }

    class Holder(private val binding: EmojiUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            binding.emoji = emojiShop
            binding.executePendingBindings()
        }
    }
}