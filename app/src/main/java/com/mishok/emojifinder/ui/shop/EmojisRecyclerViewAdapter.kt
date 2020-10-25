package com.mishok.emojifinder.ui.shop

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.databinding.EmojiShopItemBinding
import kotlinx.android.synthetic.main.emoji_shop_item.view.*

class EmojisRecyclerViewAdapter(
    private val onClickListener: OnShopItemClickListener,

    private val progress: LottieAnimationView?,
    private val isShop: Boolean
) :
    RecyclerView.Adapter<EmojisRecyclerViewAdapter.ShopEmojiViewHolder>() {

    var adapterlist : AsyncListDiffer<EmojiShopModel>
    private var userEmojis : List<EmojiShopModel?> ?= null
    private var avatar : String ?= null

    private val CALLBACK = object : DiffUtil.ItemCallback<EmojiShopModel>() {
        override fun areItemsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
            return oldItem.text == newItem.text
        }
    }

    init {
        adapterlist = AsyncListDiffer(this, CALLBACK)
        adapterlist.addListListener { previousList, currentList ->
            if(previousList != currentList){
                progress?.visibility = View.INVISIBLE
            }
        }
    }

    lateinit var fullList : MutableList<EmojiShopModel?>
    var tmpList : MutableList<EmojiShopModel?> = mutableListOf()

    fun shopSubmitList(
        data: List<EmojiShopModel?>,
        userEmojis: List<EmojiShopModel?>
    ) {
        this.userEmojis = userEmojis
        this.fullList = data as MutableList<EmojiShopModel?>
        createList(userEmojis, data)
    }

    private fun createList(
        data: List<EmojiShopModel?>,
        shop: List<EmojiShopModel?>
    ) {
        for(emoji in shop){
            for(userEmoji in data){
                if(emoji?.text == userEmoji?.text){
                    emoji?.group = "Your"
                }
            }
        }
        adapterlist.submitList(shop)
    }

    fun filter(categories: MutableList<String>) {
        tmpList = mutableListOf()
        if(categories.size != 0){
            progress?.visibility = View.VISIBLE
            for(category in categories) {
                for(emoji in fullList){
                    if(emoji?.group == category){
                        tmpList.add(emoji)
                    }
                }
            }
            adapterlist.submitList(tmpList)
        } else {
            adapterlist.submitList(fullList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ShopEmojiViewHolder {
        return ShopEmojiViewHolder(
            EmojiShopItemBinding.inflate(LayoutInflater.from(parent.context)), userEmojis,isShop,avatar
        )
    }

    override fun onBindViewHolder(holder: ShopEmojiViewHolder, position: Int){
        val emoji = adapterlist.currentList[position]
        if(holder.itemView.emoji_view.backgroundTintList ==
            holder.itemView.resources.getColorStateList(R.color.green_color)) {
            holder.setIsRecyclable(false)
        }
        holder.itemView.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.itemView.emoji_view.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.bind(emoji)
    }

    fun resetFilters() {
        adapterlist.submitList(fullList)
    }

    class ShopEmojiViewHolder(
        private val binding: EmojiShopItemBinding,
        private val userEmojis: List<EmojiShopModel?>?,
        val shop: Boolean,
        val avatar : String?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emojiShop: EmojiShopModel?) {
            findUserEmojis(emojiShop)
            binding.emoji = emojiShop
            binding.executePendingBindings()
        }

        private fun findUserEmojis(emojiShop: EmojiShopModel?) {
            if(!userEmojis.isNullOrEmpty() && shop) {
                for(emojiView in userEmojis) {
                    if (emojiView?.text == emojiShop?.text) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.emojiView.backgroundTintList =
                                binding.emojiView.resources.getColorStateList(R.color.green_color, null)
                            emojiShop?.group = "Your"
                        }
                    }
                }
            }

            if(!shop){
                if (emojiShop?.text == avatar) {
                    binding.emojiView.backgroundTintList =
                        binding.emojiView.resources.getColorStateList(R.color.green_color)
                } else {
                    binding.emojiView.backgroundTintList = null
                }
            }
        }
    }

    class OnShopItemClickListener(val clickListener: (track: EmojiShopModel?) -> Unit) {
        fun onClick(track: EmojiShopModel) = clickListener(track)
    }

    override fun getItemCount(): Int {
        return adapterlist.currentList.size
    }

    fun getGeneratedEmoji(generatedEmoji: String): EmojiShopModel? {
        for(emoji in adapterlist.currentList){
            if(emoji.text == generatedEmoji){
               return emoji
            }
        }
        return null
    }

    fun submitUserList(avatar: String?, data: List<EmojiShopModel?>) {
        this.avatar = avatar
        this.userEmojis = data
        adapterlist.submitList(data)
    }

    fun changeEmoji(generatedEmoji: String) {
        val emoji = adapterlist.currentList.find {
            emojiShopModel -> emojiShopModel.text == generatedEmoji
        }
        emoji?.group = "Your"
    }

    fun removeUserEmoji(emojiShop: EmojiShopModel?) {
        tmpList.remove(emojiShop)
        val emoji = fullList.find {
            it?.text == emojiShop?.text
        }
        emoji?.group = "Other"

        notifyDataSetChanged()
    }
}