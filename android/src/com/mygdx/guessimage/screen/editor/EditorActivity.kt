package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.screen.base.BaseActivity
import org.jetbrains.anko.UI
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent

class EditorActivity : BaseActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idObject = View.generateViewId()
        val idObjects = View.generateViewId()
        setContentView(UI {
            linearLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idObject
                }.lparams(0, matchParent, 3f)
                frameLayout {
                    id = idObjects
                }.lparams(0, matchParent, 1f)
            }
        }.view)
        fragmentManager.transact {
            add(idObject, ObjectFragment(), ObjectFragment::class.java.simpleName)
            add(idObjects, ObjectsFragment(), ObjectsFragment::class.java.simpleName)
        }
    }
}