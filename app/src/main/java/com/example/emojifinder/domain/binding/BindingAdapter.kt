package com.example.emojifinder.domain.binding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.math.BigDecimal
import java.math.RoundingMode

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["app:score", "app:max"], requireAll = true)
fun bindProgress(textView: TextView, score : Int, max : Int){
    val progress : Double = ((score.toDouble() / max.toDouble()) * 100)
    textView.text = BigDecimal(progress).setScale(1, RoundingMode.HALF_DOWN).toString()
}