package com.mishok.emojifinder.ui.constructor.choosePhoto

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mishok.emojifinder.R
import com.mishok.emojifinder.databinding.ItemPhotoPickerBinding

class PhotoPickerViewHolder(private val binding : ItemPhotoPickerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: PhotoPickerModel?) {
        Glide.with(binding.root)
            .load(photo?.uri)
            .placeholder(R.drawable.icons8_save_26px)
            .into(binding.photoImageView)
        binding.executePendingBindings()
    }
}