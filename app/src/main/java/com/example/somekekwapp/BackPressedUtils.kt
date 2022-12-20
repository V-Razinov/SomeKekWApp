package com.example.somekekwapp

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

inline fun ComponentActivity.addBackPressedCallback(
    crossinline action: () -> Unit
): OnBackPressedCallback {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = action()
    }
    onBackPressedDispatcher.addCallback(this, callback)
    return callback
}

inline fun Fragment.addBackPressedCallback(
    crossinline action: () -> Unit
): OnBackPressedCallback = requireActivity().addBackPressedCallback(action)