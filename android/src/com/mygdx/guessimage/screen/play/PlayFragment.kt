package com.mygdx.guessimage.screen.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.screen.DrawingFragment
import com.mygdx.guessimage.screen.base.BaseFragment
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI

class PlayFragment : BaseFragment() {

    private val idDrawing = View.generateViewId()

    private lateinit var playModel: PlayModel

    private lateinit var drawingFragment: DrawingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playModel = ViewModelProvider(requireActivity()).get(PlayModel::class.java)
        drawingFragment = DrawingFragment.newInstance(Mode.PLAY, playModel.puzzle.filename)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            frameLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idDrawing
                }.lparams(matchParent, matchParent)
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.transact {
            add(idDrawing, drawingFragment, DrawingFragment.TAG)
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