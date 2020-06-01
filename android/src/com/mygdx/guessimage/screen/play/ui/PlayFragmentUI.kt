package com.mygdx.guessimage.screen.play.ui

import android.view.View
import android.view.ViewGroup
import com.mygdx.guessimage.screen.play.PlayFragment
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent

class PlayFragmentUI : AnkoComponent<PlayFragment> {

    override fun createView(ui: AnkoContext<PlayFragment>): View = with(ui) {
        frameLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            frameLayout {
                id = owner.idDrawing
            }.lparams(matchParent, matchParent)
        }
    }
}