package com.mishok.emojifinder.ui.game.arcade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mishok.emojifinder.databinding.EmojiGameItemBinding
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import kotlinx.android.synthetic.main.emoji_game_item.view.*


class GameRecyclerViewAdapter(private val onClickListener: OnEmojiClickListener) :
    RecyclerView.Adapter<GameRecyclerViewAdapter.GameViewHolder>() {

    var emojis : MutableList<EmojiShopModel?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(EmojiGameItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    fun setData(oneLevelEmojis: MutableList<EmojiShopModel?>) {
        this.emojis = oneLevelEmojis
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val emoji = emojis[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(emoji!!)
            holder.itemView.emojiStars.playAnimation()
        }
        holder.itemView.emoji_game_view.setOnClickListener {
            onClickListener.onClick(emoji!!)
            holder.itemView.emojiStars.playAnimation()
        }
        holder.bind(emoji)
    }

    class GameViewHolder(private val binding: EmojiGameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            binding.emojiGameView.text = emojiShop?.text
            binding.executePendingBindings()
        }
    }

    class OnEmojiClickListener(val clickListener: (emojiShop: EmojiShopModel?) -> Unit) {
        fun onClick(emojiShop: EmojiShopModel) = clickListener(emojiShop)
    }

    override fun getItemCount(): Int {
        return emojis.size
    }

    fun clear() {
        emojis.clear()
    }
}