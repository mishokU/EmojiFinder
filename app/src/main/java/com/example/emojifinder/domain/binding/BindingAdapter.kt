package com.example.emojifinder.domain.binding

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.emoji.widget.EmojiAppCompatEditText
import com.bumptech.glide.Glide
import com.example.emojifinder.data.db.local.models.Daily
import com.example.emojifinder.data.db.remote.service.FirebaseInit
import com.example.emojifinder.domain.glide.GlideApp
import java.math.BigDecimal
import java.math.RoundingMode

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["app:score", "app:max"], requireAll = true)
fun bindProgress(textView: TextView, score: Int, max: Int) {
    val progress: Double = ((score.toDouble() / max.toDouble()) * 100)
    textView.text = BigDecimal(progress).setScale(1, RoundingMode.HALF_DOWN).toString()
}

@BindingAdapter("daily_type")
fun bindDailyType(view: EmojiAppCompatEditText, type: Daily) {
    when (type) {
        Daily.EMOJI -> view.setText("\uD83D\uDE00")
        Daily.BOX -> view.setText("\uD83C\uDF81")
        Daily.EMOS -> view.setText("\uD83D\uDCB0")
    }
}

@BindingAdapter("image")
fun bindImage(imageView: ImageView, url: String?) {
    if(url != "" && url != null){
        GlideApp.with(imageView.context)
            .load(FirebaseInit.mFireStorage.child(url))
            .into(imageView)
    }
}