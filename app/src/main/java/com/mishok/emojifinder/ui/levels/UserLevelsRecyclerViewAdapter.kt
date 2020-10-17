package com.mishok.emojifinder.ui.levels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mishok.emojifinder.databinding.UserLevelModelBinding
import com.mishok.emojifinder.ui.categories.SmallLevelModel

class UserLevelsRecyclerViewAdapter(private val onClickListener: OnLevelClickListener,
                                    private val onLongClickListener : OnDeleteClickListener
) : ListAdapter<SmallLevelModel, UserLevelsRecyclerViewAdapter.UserLevelViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<SmallLevelModel>() {

        override fun areItemsTheSame(oldItem: SmallLevelModel, newItem: SmallLevelModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SmallLevelModel, newItem: SmallLevelModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : UserLevelViewHolder {
        return UserLevelViewHolder(
            UserLevelModelBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: UserLevelViewHolder, position: Int){
        val emoji = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(emoji)
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onLongClick(emoji)
            true
        }
        holder.bind(emoji)
    }

    class UserLevelViewHolder(private val binding: UserLevelModelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(level: SmallLevelModel?) {
            binding.level = level
            binding.executePendingBindings()
        }
    }

    class OnLevelClickListener(val clickListener: (level : SmallLevelModel?) -> Unit) {
        fun onClick(level: SmallLevelModel) = clickListener(level)
    }

    class OnDeleteClickListener(val longClickListener: (level: SmallLevelModel) -> Unit){
        fun onLongClick(level: SmallLevelModel) = longClickListener(level)
    }


}