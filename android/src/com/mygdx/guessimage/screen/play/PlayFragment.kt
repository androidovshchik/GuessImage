package com.mygdx.guessimage.screen.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.Gdx
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.DrawFragment
import com.mygdx.guessimage.screen.base.BaseFragment
import com.mygdx.guessimage.screen.play.ui.PlayFragmentUI
import org.jetbrains.anko.AnkoContext

class PlayFragment : BaseFragment() {

    private lateinit var playModel: PlayModel

    private lateinit var drawFragment: DrawFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playModel = ViewModelProvider(requireActivity()).get(PlayModel::class.java)
        drawFragment = DrawFragment.newInstance(Mode.PLAY, playModel.puzzle.filename)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return PlayFragmentUI().createView(AnkoContext.create(requireActivity(), this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.transact {
            add(PlayFragmentUI.idDrawing, drawFragment, DrawFragment.TAG)
        }
    }

    fun setObjects(items: List<ObjectEntity>) {
        Gdx.app.postRunnable {
            items.forEach {
                drawFragment.guessImage.addFrame(it.id, it.x0, it.y0, it.width, it.height)
            }
        }
    }

    companion object {

        val TAG = PlayFragment::class.java.simpleName

        fun newInstance(): PlayFragment {
            return PlayFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}