package com.mygdx.guessimage.screen.play

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import org.jetbrains.anko.*

class PlayActivity : BaseActivity(), AndroidFragmentApplication.Callbacks {

    private lateinit var playModel: PlayModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playModel = ViewModelProvider(this).get(PlayModel::class.java).also {
            it.puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
        }
        val idPlay = View.generateViewId()
        val idList = View.generateViewId()
        setContentView(UI {
            linearLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idPlay
                }.lparams(0, matchParent, 3f)
                frameLayout {
                    id = idList
                }.lparams(0, matchParent, 1f)
            }
        }.view)
        supportFragmentManager.transact {
            add(idPlay, PlayFragment.newInstance(), PlayFragment.TAG)
            add(idList, ListFragment.newInstance(), ListFragment.TAG)
        }
    }

    fun showWin() {
        alert("Вы выиграли!") {
            okButton {
                finish()
            }
        }.show()
    }

    override fun exit() {}
}