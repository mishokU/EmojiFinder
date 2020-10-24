package com.mishok.emojifinder.ui.constructor.choosePhoto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mishok.emojifinder.databinding.ItemPhotoPickerBinding
import kotlinx.android.synthetic.main.item_photo_picker.view.*

class PhotoPickerAdapter(
    private val onClickListener: OnPhotoPick
) : RecyclerView.Adapter<PhotoPickerViewHolder>() {

    var items: MutableList<PhotoPickerModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPickerViewHolder {
        return PhotoPickerViewHolder(ItemPhotoPickerBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PhotoPickerViewHolder, position: Int) {
        val photo = items[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(photo)
        }
        holder.itemView.photoImageView.setOnClickListener{
            onClickListener.onClick(photo)
        }
        holder.bind(photo)
    }

    fun setData(data: MutableList<PhotoPickerModel>) {
        this.items = data
        this.notifyDataSetChanged()
    }

    open class OnPhotoPick(val clickListener: (photo: PhotoPickerModel) -> Unit) {
        fun onClick(photo: PhotoPickerModel) = clickListener(photo)
    }
}