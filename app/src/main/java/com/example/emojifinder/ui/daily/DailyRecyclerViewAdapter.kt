package com.example.emojifinder.ui.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.data.db.local.models.DailyModel
import com.example.emojifinder.databinding.DailyItemBinding
import com.example.emojifinder.databinding.UserLevelModelBinding
import com.example.emojifinder.ui.categories.SmallLevelModel
import kotlinx.android.synthetic.main.daily_item.view.*

class DailyRecyclerViewAdapter :
    ListAdapter<DailyModel, DailyRecyclerViewAdapter.UserLevelViewHolder>(DiffCallback) {

    private var day: Int = 1

    companion object DiffCallback : DiffUtil.ItemCallback<DailyModel>() {

        override fun areItemsTheSame(oldItem: DailyModel, newItem: DailyModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DailyModel, newItem: DailyModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserLevelViewHolder {
        return UserLevelViewHolder(
            DailyItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: UserLevelViewHolder, position: Int) {
        val emoji = getItem(position)
        if (emoji.id != day) {
            holder.itemView.lin_daily.alpha = 0.5f
        }
        holder.bind(emoji)
    }

    fun setDay(day: Int) {
        this.day = day
    }

    class UserLevelViewHolder(private val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(level: DailyModel?) {
            binding.daily = level
            binding.executePendingBindings()
        }
    }
}