package com.mygdx.guessimage.screen.editor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import org.jetbrains.anko.UI
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent

@Suppress("DEPRECATION")
class EditorActivity : BaseActivity() {

    private val idObject = View.generateViewId()
    private val idObjects = View.generateViewId()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(UI {
            linearLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idObject
                    setBackgroundColor(Color.parseColor("#E0E0E0"))
                }.lparams(0, matchParent, 3f)
                frameLayout {
                    id = idObjects
                }.lparams(0, matchParent, 1f)
            }
        }.view)
        fragmentManager.transact {
            add(idObjects, ObjectsFragment.newInstance(), ObjectsFragment.TAG)
        }
    }

    fun editObject(obj: ObjectEntity) {
        fragmentManager.transact {
            replace(idObject, ObjectFragment.newInstance(obj), ObjectFragment.TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragmentManager.findFragmentByTag(ObjectFragment.TAG)
            ?.onActivityResult(requestCode, resultCode, data)
        fragmentManager.findFragmentByTag(ObjectsFragment.TAG)
            ?.onActivityResult(requestCode, resultCode, data)
    }
}