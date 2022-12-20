package com.example.somekekwapp

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.Px
import androidx.core.view.*

fun View.updateMargins(
    @Px start: Int = marginStart,
    @Px top: Int = marginTop,
    @Px end: Int = marginEnd,
    @Px bottom: Int = marginBottom,
) {
    val lp = layoutParams as? MarginLayoutParams ?: return
    layoutParams = lp.apply {
        updateMarginsRelative(start = start, top = top, end = end, bottom = bottom)
    }
}