package com.mygdx.guessimage.extension

import android.view.ViewManager
import androidx.core.widget.NestedScrollView
import org.jetbrains.anko.custom.ankoView

fun ViewManager.nestedScrollView(theme: Int = 0) = nestedScrollView(theme) {}

inline fun ViewManager.nestedScrollView(theme: Int = 0, init: NestedScrollView.() -> Unit) =
    ankoView({ NestedScrollView(it) }, theme, init)