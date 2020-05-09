package com.mygdx.guessimage.screen.editor

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.screen.base.BaseActivity
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent

class EditorActivity : BaseActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idObject = View.generateViewId()
        val idObjects = View.generateViewId()
        setContentView(horizontalLayout {
            lParams(matchParent, matchParent)
            addView(frameLayout {
                id = idObject
                lParams(0, matchParent, weight = 1f)
                setBackgroundColor(Color.RED)
            })
            addView(frameLayout {
                id = idObjects
                lParams(0, matchParent, weight = 3f)
                setBackgroundColor(Color.GREEN)
            })
        })
        fragmentManager.transact {
            add(idObject, ObjectFragment(), ObjectFragment::class.java.simpleName)
            add(idObjects, ObjectsFragment(), ObjectsFragment::class.java.simpleName)
        }
    }
}