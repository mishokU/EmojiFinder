package com.example.emojifinder.ui.constructor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.databinding.EmojiConstructorItemBinding
import kotlinx.android.synthetic.main.emoji_constructor_item.view.*

class LevelConstructorRecyclerViewAdapter(private val onClickListener : OnEmojiClickListener) : ListAdapter<EmojiShopModel,
        LevelConstructorRecyclerViewAdapter.KeyboardViewHolder>(
    DiffCallback
) {

    private var element : com.example.emojifinder.ui.shop.EmojiShopModel ?= null
    private var order = 0

    companion object DiffCallback: DiffUtil.ItemCallback<EmojiShopModel>()  {

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
        val button = holder.itemView.emoji_constructor_btn
        holder.itemView.setOnClickListener {
            handleEmoji(button, emoji, position)
        }
        holder.itemView.emoji_constructor_btn.setOnClickListener {
            handleEmoji(button, emoji, position)
        }
        holder.bind(emoji)
    }

    private fun handleEmoji(
        button: EmojiAppCompatButton,
        emoji: EmojiShopModel,
        position: Int
    ) {
        if(element != null){
            emoji.unicode = element?.text!!
            if(button.text.toString() == ""){
                button.text = element?.text

                emoji.order = order
                order++
            } else {
                // If position of deleted emoji is first
                // increase all emojis by 1 in order
                when {
                    position == 0 -> {
                        for(item in currentList){
                            item.order -= 1
                        }
                        order--
                        emoji.order = -1
                        button.text = ""
                        // if position in the middle
                    }
                    position != currentList.size - 1 -> {
                        for(item in currentList.subList(position, currentList.size - 1)){
                            item.order -= 1
                        }
                        order--
                        emoji.order = -1
                        button.text = ""
                    }
                    else -> {
                        emoji.order = -1
                        button.text = ""
                        order--
                    }
                }
            }
            for(item in currentList){
                if(item.unicode != ""){
                    println(item)
                }
            }
            onClickListener.onClick(emoji)
        }
    }

    fun setActiveElement(it: com.example.emojifinder.ui.shop.EmojiShopModel?) {
        if (it != null) {
            this.element = it
        }
    }

    fun resetOrder() {
        order = 0
        for(item in currentList){
            item.order = 0
        }
    }

    fun isEmptyLevel(): Boolean {
        var items = 0
        for(item in currentList){
            if(item.unicode != ""){
                items++
            }
        }
        return items == 0
    }

    fun setLevelTitleToEmojis(title: String) {
        for(emoji in currentList){
            emoji.title = title
        }
    }

    fun setOrder(){
        var tmpMax = 0
        for(item in currentList){
            if(item.order > tmpMax){
                tmpMax = item.order
            }
        }
        order = tmpMax
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