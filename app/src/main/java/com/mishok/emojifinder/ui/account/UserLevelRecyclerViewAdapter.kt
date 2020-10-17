package com.mishok.emojifinder.ui.account

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.mishok.emojifinder.databinding.AccountLevelItemBinding

class UserLevelRecyclerViewAdapter(
    private val onClickListener: OnLevelClickListener,
    private val context: Context
) : ListAdapter<UserLevelStatistics,
        UserLevelRecyclerViewAdapter.LevelViewHolder>(
    DiffCallback
) {

    companion object DiffCallback : DiffUtil.ItemCallback<UserLevelStatistics>() {

        override fun areItemsTheSame(
            oldItem: UserLevelStatistics,
            newItem: UserLevelStatistics
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserLevelStatistics,
            newItem: UserLevelStatistics
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        return LevelViewHolder(
            context,
            AccountLevelItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(level)
            holder.expand()
        }
        holder.bind(level)
    }

    class LevelViewHolder(
        private val context: Context,
        private val binding: AccountLevelItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var expand = true

        fun bind(level: UserLevelStatistics?) {
            binding.level = level
            binding.executePendingBindings()

        }

        fun expand() {
            if (expand) {
                binding.extraStatistic.visibility = View.VISIBLE
                binding.expandExtraLevelStatisticBtn.icon = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.icons8_back_16px_up, null
                )
                expand = false
            } else {
                binding.extraStatistic.visibility = View.GONE
                binding.expandExtraLevelStatisticBtn.icon = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.icons8_back_16px, null
                )
                expand = true
            }
        }
    }

    class OnLevelClickListener(val clickListener: (level: UserLevelStatistics?) -> Unit) {
        fun onClick(level: UserLevelStatistics) = clickListener(level)
    }
}