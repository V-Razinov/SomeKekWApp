package com.example.somekekwapp

import android.graphics.Rect
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.*
import androidx.core.view.WindowInsetsCompat.Type.InsetsType

// status bar
inline fun View.setApplyStatusBarInsetsListener(
    crossinline applyInsets: View.(insets: Insets) -> Unit,
) = setApplyWindowInsetsListener(
    insetsType = WindowInsetsCompat.Type.statusBars(),
    applyInsets = applyInsets
)

inline fun View.setApplyStatusBarInsetsListener(
    crossinline applyInsets: View.(originalMargins: Rect, originalPaddings: Rect, insets: Insets) -> Unit,
) = setApplyWindowInsetsListener(
    insetsType = WindowInsetsCompat.Type.statusBars(),
    applyInsets = applyInsets
)

// nav bar
inline fun View.setApplyNavBarInsetsListener(
    crossinline applyInsets: View.(insets: Insets) -> Unit,
) = setApplyWindowInsetsListener(
    insetsType = WindowInsetsCompat.Type.navigationBars(),
    applyInsets = applyInsets
)

inline fun View.setApplyNavBarInsetsListener(
    crossinline applyInsets: View.(originalMargins: Rect, originalPaddings: Rect, insets: Insets) -> Unit,
) = setApplyWindowInsetsListener(
    insetsType = WindowInsetsCompat.Type.navigationBars(),
    applyInsets = applyInsets
)

//-----------
inline fun View.setApplyWindowInsetsListener(
    @InsetsType insetsType: Int,
    crossinline applyInsets: View.(insets: Insets) -> Unit,
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        view.applyInsets(windowInsets.getInsets(insetsType))
        windowInsets
    }
}

inline fun View.setApplyWindowInsetsListener(
    @InsetsType insetsType: Int,
    crossinline applyInsets: View.(originalMargins: Rect, originalPaddings: Rect, insets: Insets) -> Unit,
) {
    val originalMargins = Rect(marginStart, marginTop, marginRight, marginBottom)
    val originalPaddings = Rect(paddingStart, paddingTop, paddingRight, paddingBottom)
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        view.applyInsets(originalMargins, originalPaddings, windowInsets.getInsets(insetsType))
        windowInsets
    }
}