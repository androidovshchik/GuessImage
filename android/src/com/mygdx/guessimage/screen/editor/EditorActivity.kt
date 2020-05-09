package com.mygdx.guessimage.screen.editor

import android.annotation.SuppressLint
import android.os.Bundle
import com.mygdx.guessimage.screen.base.BaseActivity
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent

class EditorActivity : BaseActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(horizontalLayout {
            lParams(matchParent, matchParent)
        })
    }
}