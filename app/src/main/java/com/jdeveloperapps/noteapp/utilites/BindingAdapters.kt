package com.jdeveloperapps.noteapp.utilites

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import java.io.File

@BindingAdapter("android:colorBackground")
fun setBackgroundDrawable(view: View, color: String?) {
    val gradientDrawable = view.background as GradientDrawable
    color?.let {
        gradientDrawable.setColor(Color.parseColor(color))
    }
}

@BindingAdapter("android:loadImage")
fun setImageSrc(view: ImageView, url: String?) {
    url?.let {
        view.load(Uri.fromFile(File(url)))
    }
}