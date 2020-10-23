package com.mishok.emojifinder.ui.game.campaign.gameAlerts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel
import com.mishok.emojifinder.databinding.EmojiGameItemBinding
import kotlinx.android.synthetic.main.emoji_game_item.view.*


class GameKeyBoardRecyclerViewAdapter(private val onClickListener: OnEmojiClickListener) :
    RecyclerView.Adapter<GameKeyBoardRecyclerViewAdapter.KeyboardViewHolder>() {

    var keyboardEmojis : MutableList<EmojiShopModel?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyboardViewHolder {
        return KeyboardViewHolder(
            EmojiGameItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: KeyboardViewHolder, position: Int) {
        val emoji = keyboardEmojis[position]
        if(emoji != null){
            holder.itemView.setOnClickListener {
                onClickListener.onClick(emoji)
            }
            holder.itemView.emoji_game_view.setOnClickListener {
                onClickListener.onClick(emoji)
            }
            holder.bind(emoji)
        }
    }

    class KeyboardViewHolder(private val binding: EmojiGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            binding.emojiGameView.text = emojiShop?.unicode
            binding.executePendingBindings()
        }
    }

    class OnEmojiClickListener(val clickListener: (emojiShop: EmojiShopModel?) -> Unit) {
        fun onClick(emojiShop: EmojiShopModel) = clickListener(emojiShop)
    }

    override fun getItemCount(): Int {
        return keyboardEmojis.size
    }

    fun setData(emojis: MutableList<EmojiShopModel?>) {
        this.keyboardEmojis = emojis
        notifyDataSetChanged()
    }
}