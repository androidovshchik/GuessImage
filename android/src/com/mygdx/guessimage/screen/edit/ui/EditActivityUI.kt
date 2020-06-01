package com.mygdx.guessimage.screen.edit.ui

import android.view.View
import android.view.ViewGroup
import com.mygdx.guessimage.screen.edit.EditActivity
import org.jetbrains.anko.*

class EditActivityUI : AnkoComponent<EditActivity> {

    override fun createView(ui: AnkoContext<EditActivity>): View = with(ui) {
        linearLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            frameLayout {
                id = idEdit
            }.lparams(0, matchParent, 3f)
            frameLayout {
                id = idPanel
            }.lparams(0, matchParent, 1f)
        }
    }

    companion object {

        val idEdit = View.generateViewId()
        val idPanel = View.generateViewId()
    }
}