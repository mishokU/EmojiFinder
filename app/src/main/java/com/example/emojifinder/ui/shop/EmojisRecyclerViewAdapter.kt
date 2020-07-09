package com.example.emojifinder.ui.shop

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.example.emojifinder.R
import com.example.emojifinder.databinding.EmojiShopItemBinding
import kotlinx.android.synthetic.main.emoji_shop_item.view.*

class EmojisRecyclerViewAdapter(
    private val onClickListener: OnShopItemClickListener,
    private val progress: LottieAnimationView?,
    val isShop: Boolean
) :
    RecyclerView.Adapter<EmojisRecyclerViewAdapter.ShopEmojiViewHolder>() {

    private var adapterlist : AsyncListDiffer<EmojiShopModel>
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

    private lateinit var fullList : List<EmojiShopModel?>

    fun shopSubmitList(
        data: List<EmojiShopModel?>,
        userEmojis: List<EmojiShopModel?>
    ) {
        this.userEmojis = userEmojis
        this.fullList = data
        this.adapterlist.submitList(data)
    }

    fun filter(categories: MutableList<String>) {
        val list : MutableList<EmojiShopModel> = mutableListOf()
        if(categories.size != 0){
            progress?.visibility = View.VISIBLE
            for(category in categories) {
                for(emoji in fullList){
                    if(emoji?.group == category){
                        list.add(emoji)
                    }
                }
            }
            adapterlist.submitList(list)
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
        val userEmojis: List<EmojiShopModel?>?,
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

    fun submitList(data: List<EmojiShopModel?>) {
        adapterlist.submitList(data)
    }

    fun getGeneratedEmoji(generatedEmoji: String): EmojiShopModel? {
        for(emoji in adapterlist.currentList){
            if(emoji.text == generatedEmoji){
               return emoji
            }
        }
        return null
    }

    fun submitUserList(avatar: String, data: List<EmojiShopModel?>) {
        this.avatar = avatar
        this.userEmojis = data
        adapterlist.submitList(data)
    }

}