package com.mygdx.guessimage.screen.editor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import org.jetbrains.anko.UI
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent

@Suppress("DEPRECATION")
class EditorActivity : BaseActivity() {

    private lateinit var puzzleModel: PuzzleModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        puzzleModel = ViewModelProvider(this).get(PuzzleModel::class.java)
        puzzleModel.puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
        val idObject = View.generateViewId()
        val idObjects = View.generateViewId()
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
        supportFragmentManager.transact {
            add(idObject, ObjectFragment.newInstance(), ObjectFragment.TAG)
            add(idObjects, ObjectsFragment.newInstance(), ObjectsFragment.TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*supportFragmentManager.findFragmentByTag(ObjectFragment.TAG)
            ?.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentByTag(ObjectsFragment.TAG)
            ?.onActivityResult(requestCode, resultCode, data)*/
    }
}