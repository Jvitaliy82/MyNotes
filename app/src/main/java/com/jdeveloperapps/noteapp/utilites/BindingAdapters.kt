package com.jdeveloperapps.noteapp.utilites

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:colorBackground")
fun setBackgroundDrawable(view: View, color: String?) {
    val gradientDrawable = view.background as GradientDrawable
    color?.let {
        gradientDrawable.setColor(Color.parseColor(color))
    }
}