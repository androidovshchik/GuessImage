package com.mygdx.guessimage.screen.edit.ui

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.mygdx.guessimage.R
import com.mygdx.guessimage.screen.edit.EditFragment
import org.jetbrains.anko.*

class EditFragmentUI : AnkoComponent<EditFragment> {

    override fun createView(ui: AnkoContext<EditFragment>): View = with(ui) {
        frameLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            frameLayout {
                id = idDrawing
            }.lparams(matchParent, matchParent)
            owner.buttonSelect = button {
                text = context.getString(R.string.upload)
                setOnClickListener {
                    owner.openPicker()
                }
            }.lparams(wrapContent, wrapContent, Gravity.CENTER)
        }
    }

    companion object {

        val idDrawing = View.generateViewId()
    }
}