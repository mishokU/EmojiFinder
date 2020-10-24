package com.mishok.emojifinder.ui.constructor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.databinding.EmojiGameItemBinding
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import kotlinx.android.synthetic.main.emoji_game_item.view.*


class AllEmojisRecyclerViewAdapter(
    private val onClickListener: OnEmojiClickListener,
    private val progress: LottieAnimationView
) : RecyclerView.Adapter<AllEmojisRecyclerViewAdapter.KeyboardViewHolder>() {

    private var prevElement : Int = 0
    var adapterlist : MutableList<EmojiShopModel?> = mutableListOf()
    private lateinit var fullList : MutableList<EmojiShopModel?>


    fun allEmojisSubmitList(data: MutableList<EmojiShopModel?>) {
        this.fullList = data
        this.adapterlist = data
        this.notifyDataSetChanged()
    }

    fun getCurrentElement() : Int {
        return prevElement
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : KeyboardViewHolder {
        return KeyboardViewHolder(EmojiGameItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: KeyboardViewHolder, position: Int){
        val emoji = adapterlist.get(index = position)
        val button = holder.itemView.emoji_game_view
        holder.setIsRecyclable(true)
//        if(holder.itemView.emoji_game_view.background ==
//            ContextCompat.getDrawable(button.context, R.drawable.checked_emoji_style
//            )) {
//            holder.setIsRecyclable(false)
//        }
        holder.itemView.setOnClickListener {
            //setChecked(button)
            onClickListener.onClick(emoji!!, prevElement)
            prevElement = holder.layoutPosition
        }
        button.setOnClickListener {
            //setChecked(button)
            onClickListener.onClick(emoji!!, prevElement)
            prevElement = holder.layoutPosition
        }
        holder.bind(emoji)
    }

    fun resetBackground(viewHolder: RecyclerView.ViewHolder?) {
        val button = viewHolder?.itemView?.emoji_game_view
        if (button != null) {
            button.background = ContextCompat.getDrawable(
                button.context,
                R.color.background_color
            )
        }
    }

    override fun getItemCount(): Int {
        return adapterlist.size
    }

    private fun setChecked(button: EmojiAppCompatButton) {
        button.background = ContextCompat.getDrawable(
            button.context,
            R.drawable.checked_emoji_style
        )
    }

    fun resetFilters() {
        adapterlist = fullList
    }

    fun filter(categories: MutableList<String>) {
        val list : MutableList<EmojiShopModel?> = mutableListOf()
        if(categories.size != 0){
            progress.visibility = View.VISIBLE
            for(category in categories) {
                for(emoji in fullList){
                    if(emoji?.group == category){
                        list.add(emoji)
                    }
                }
            }
            adapterlist = list
        } else {
            adapterlist = fullList
        }
    }

    class KeyboardViewHolder(private val binding: EmojiGameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            binding.emojiGameView.text = emojiShop?.text
            binding.executePendingBindings()
        }
    }

    class OnEmojiClickListener(val clickListener: (emojiShop: EmojiShopModel?, id : Int) -> Unit) {
        fun onClick(emojiShop: EmojiShopModel, id : Int) = clickListener(emojiShop, id)
    }
}