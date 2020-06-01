package com.mygdx.guessimage.screen.play.ui

import android.view.View
import android.view.ViewGroup
import com.mygdx.guessimage.screen.play.PlayActivity
import org.jetbrains.anko.*

class PlayActivityUI : AnkoComponent<PlayActivity> {

    override fun createView(ui: AnkoContext<PlayActivity>): View = with(ui) {
        linearLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            frameLayout {
                id = idPlay
            }.lparams(0, matchParent, 3f)
            frameLayout {
                id = idList
            }.lparams(0, matchParent, 1f)
        }
    }

    companion object {

        val idPlay = View.generateViewId()
        val idList = View.generateViewId()
    }
}