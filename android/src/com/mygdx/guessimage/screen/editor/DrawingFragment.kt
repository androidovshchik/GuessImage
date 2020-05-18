package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.GuessImage

@Suppress("MemberVisibilityCanBePrivate")
class DrawingFragment : AndroidFragmentApplication() {

    private lateinit var puzzleModel: PuzzleModel

    lateinit var guessImage: GuessImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        puzzleModel = ViewModelProvider(requireActivity()).get(PuzzleModel::class.java)
        guessImage = GuessImage(puzzleModel.mode, object : GuessImage.Listener {
        })
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return initializeForView(guessImage)
    }

    companion object {

        val TAG = ObjectFragment::class.java.simpleName

        fun newInstance(): DrawingFragment {
            return DrawingFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}