package com.example.emojifinder.ui.rating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.databinding.RatingUserItemBinding

class RatingRecyclerViewAdapter : ListAdapter<MainAccountModel, RatingRecyclerViewAdapter.RatingViewHolder>(DiffCallback) {

    var users = 0

    companion object DiffCallback: DiffUtil.ItemCallback<MainAccountModel>() {

        override fun areItemsTheSame(oldItem: MainAccountModel, newItem: MainAccountModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MainAccountModel, newItem: MainAccountModel): Boolean {
            return oldItem.email == newItem.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RatingViewHolder {

        return RatingViewHolder(
            RatingUserItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int){
        val emoji = getItem(position)
        users++
        holder.bind(emoji,users)
    }

    class RatingViewHolder(private val binding: RatingUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: MainAccountModel?, users : Int) {
            binding.user = user
            binding.userNumberRating.text = users.toString()
            binding.executePendingBindings()
        }
    }

}