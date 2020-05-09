package com.mygdx.guessimage.extension

import android.view.ViewManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.custom.ankoView

fun ViewManager.recyclerView(theme: Int = 0) = recyclerView(theme) {}

inline fun ViewManager.recyclerView(theme: Int = 0, init: RecyclerView.() -> Unit) =
    ankoView({ RecyclerView(it) }, theme, init)