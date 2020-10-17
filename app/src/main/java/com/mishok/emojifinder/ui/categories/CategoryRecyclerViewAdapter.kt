package com.mishok.emojifinder.ui.categories

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mishok.emojifinder.databinding.CategoryCellItemBinding

class CategoryRecyclerViewAdapter(private val onClickListener : OnCategoryClickListener)
    : RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoriesViewHolder>() {

    var items: List<SmallLevelModel> = listOf()
    var currentItem : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoryCellItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int){
        holder.itemView.setOnClickListener {
            onClickListener.onClick(items[position])
        }
        holder.bind(items[position])
        currentItem = position
        Log.d("POSITION", position.toString())
    }

    fun setLevels(newItems: List<SmallLevelModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    class CategoriesViewHolder(private val binding: CategoryCellItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(category: SmallLevelModel?) {
            binding.category = category
            binding.executePendingBindings()
            binding.levelId.text = "Level " + binding.levelId.text
            binding.levelTime.text = "" + binding.levelTime.text + " sec"
        }
    }

    class OnCategoryClickListener(val clickListener: (track: SmallLevelModel?) -> Unit) {
        fun onClick(track: SmallLevelModel) = clickListener(track)
    }

    override fun getItemCount(): Int = items.size
}