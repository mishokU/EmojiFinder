package com.example.emojifinder.ui.account

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.databinding.AccountLevelItemBinding

class UserLevelRecyclerViewAdapter(
    private val onClickListener : OnLevelClickListener,
    private val context : Context
) : ListAdapter<UserLevelStatistics,
        UserLevelRecyclerViewAdapter.LevelViewHolder>(
    DiffCallback
) {

    var expandStat = true

    companion object DiffCallback: DiffUtil.ItemCallback<UserLevelStatistics>()     {

        override fun areItemsTheSame(oldItem: UserLevelStatistics, newItem: UserLevelStatistics): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserLevelStatistics, newItem: UserLevelStatistics): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : LevelViewHolder {
        return LevelViewHolder(
            context,
            AccountLevelItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int){
        val level = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(level)
            expandStat = if(expandStat) {
                holder.expand(expandStat)
                false
            } else {
                holder.expand(expandStat)
                true
            }
        }
        holder.bind(level)
    }

    fun collapseAll() {

    }

    fun expandAll() {

    }

    class LevelViewHolder(
        private val context: Context,
        private val binding: AccountLevelItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(level: UserLevelStatistics?) {
            binding.level = level
            binding.executePendingBindings()
        }

        fun expand(expand_: Boolean) {
            if(expand_){
                binding.extraStatistic.visibility = View.VISIBLE
                binding.expandExtraLevelStatisticBtn.icon = ResourcesCompat.getDrawable(context.resources,
                    R.drawable.icons8_back_16px_up, null)
            } else {
                binding.extraStatistic.visibility = View.GONE
                binding.expandExtraLevelStatisticBtn.icon = ResourcesCompat.getDrawable(context.resources,
                    R.drawable.icons8_back_16px, null)
            }
        }
    }

    class OnLevelClickListener(val clickListener: (level: UserLevelStatistics?) -> Unit) {
        fun onClick(level: UserLevelStatistics) = clickListener(level)
    }
}