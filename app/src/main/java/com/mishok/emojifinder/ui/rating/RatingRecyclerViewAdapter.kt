package com.mishok.emojifinder.ui.rating

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.data.db.remote.models.account.MainAccountModel
import com.mishok.emojifinder.databinding.RatingUserItemBinding

class RatingRecyclerViewAdapter(val context: Context) :
    ListAdapter<MainAccountModel, RatingRecyclerViewAdapter.RatingViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<MainAccountModel>() {

        override fun areItemsTheSame(
            oldItem: MainAccountModel,
            newItem: MainAccountModel
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: MainAccountModel,
            newItem: MainAccountModel
        ): Boolean {
            return oldItem.email == newItem.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        return RatingViewHolder(
            RatingUserItemBinding.inflate(LayoutInflater.from(parent.context)),
            context
        )
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val emoji = getItem(position)
        holder.bind(emoji)
    }

    class RatingViewHolder(private val binding: RatingUserItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: MainAccountModel?) {
            binding.user = user
            binding.userNumberRating.text = user?.place.toString()
            setRatingAvatars(binding.userNumberRatingPlace, user?.place)
            binding.executePendingBindings()
        }

        private fun setRatingAvatars(userNumberRatingPlace: RelativeLayout, users: Int?) {
            when (users) {
                GOLD -> userNumberRatingPlace.background =
                    ContextCompat.getDrawable(context, R.drawable.rating_gold_background)
                SILVER -> userNumberRatingPlace.background =
                    ContextCompat.getDrawable(context, R.drawable.rating_silver_background)
                BRONZE -> userNumberRatingPlace.background =
                    ContextCompat.getDrawable(context, R.drawable.rating_bronze_background)
            }
        }

        companion object {
            private const val GOLD = 1
            private const val SILVER = 2
            private const val BRONZE = 3
        }
    }

}