package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.BuildConfig
import com.mygdx.guessimage.GuessImage

class DrawingFragment : AndroidFragmentApplication() {

    lateinit var guessImage: GuessImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        guessImage = GuessImage(BuildConfig.DEBUG, object : GuessImage.Listener {
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