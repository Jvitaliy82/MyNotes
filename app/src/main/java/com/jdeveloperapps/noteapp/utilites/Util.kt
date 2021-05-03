package com.jdeveloperapps.noteapp.utilites

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View

val <T> T.exhaustive: T
    get() = this

fun setColorBackground(view: View, color: String) {
    val gradientDrawable = view.background as GradientDrawable
    gradientDrawable.setColor(Color.parseColor(color))
}