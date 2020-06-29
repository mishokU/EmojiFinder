package com.example.emojifinder.domain.binding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.emoji.widget.EmojiEditText
import com.example.emojifinder.R
import com.example.emojifinder.data.db.local.models.Daily
import java.math.BigDecimal
import java.math.RoundingMode

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["app:score", "app:max"], requireAll = true)
fun bindProgress(textView: TextView, score : Int, max : Int){
    val progress : Double = ((score.toDouble() / max.toDouble()) * 100)
    textView.text = BigDecimal(progress).setScale(1, RoundingMode.HALF_DOWN).toString()
}

@BindingAdapter("daily_type")
fun bindDailyType(view : EmojiAppCompatEditText, type : Daily){
    when(type){
        Daily.EMOJI -> view.setText("\uD83D\uDE00")
        Daily.BOX -> view.setText("\uD83C\uDF81")
        Daily.EMOS -> view.setText("\uD83D\uDCB0")
    }
}