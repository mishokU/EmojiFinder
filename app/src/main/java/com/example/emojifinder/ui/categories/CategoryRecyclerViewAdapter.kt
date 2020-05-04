package com.example.emojifinder.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.databinding.CategoryCellItemBinding

class CategoryRecyclerViewAdapter(private val onClickListener : OnCategoryClickListener) : ListAdapter<SmallLevelModel,
        CategoryRecyclerViewAdapter.CategoriesViewHolder>(
    DiffCallback
) {

    companion object DiffCallback: DiffUtil.ItemCallback<SmallLevelModel>()     {

        override fun areItemsTheSame(oldItem: SmallLevelModel, newItem: SmallLevelModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SmallLevelModel, newItem: SmallLevelModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoryCellItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int){
        val song = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(song)
        }
        holder.bind(song)
    }

    class CategoriesViewHolder(private val binding: CategoryCellItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: SmallLevelModel?) {
            binding.category = category
            binding.executePendingBindings()
        }
    }

    class OnCategoryClickListener(val clickListener: (track: SmallLevelModel?) -> Unit) {
        fun onClick(track: SmallLevelModel) = clickListener(track)
    }
}