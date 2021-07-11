package com.mishok.emojifinder.ui.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.mishok.emojifinder.data.db.local.models.DailyModel
import com.mishok.emojifinder.databinding.DailyItemBinding
import kotlinx.android.synthetic.main.daily_item.view.*

class DailyItemAdapterDelegate(
    private val day: Int
) : AbsListItemAdapterDelegate<DailyModel, DailyModel, DailyItemAdapterDelegate.UserLevelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): UserLevelViewHolder {
        return UserLevelViewHolder(
            DailyItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(item: DailyModel, holder: UserLevelViewHolder, payloads: MutableList<Any>) {
        if (item.id != day) {
            holder.itemView.lin_daily.alpha = 0.5f
        }
        holder.bind(item)
    }

    override fun isForViewType(item: DailyModel, items: MutableList<DailyModel>, position: Int): Boolean = true

    class UserLevelViewHolder(private val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(level: DailyModel?) {
            binding.daily = level
            binding.executePendingBindings()
        }
    }
}