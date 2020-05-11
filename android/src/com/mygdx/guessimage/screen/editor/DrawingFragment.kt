package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.GuessImage
import com.mygdx.guessimage.Mode

@Suppress("MemberVisibilityCanBePrivate")
class DrawingFragment : AndroidFragmentApplication() {

    lateinit var guessImage: GuessImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mode = Mode.valueOf(arguments!!.getString("mode")!!)
        guessImage = GuessImage(mode, object : GuessImage.Listener {
        })
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return initializeForView(guessImage)
    }

    companion object {

        val TAG = ObjectFragment::class.java.simpleName

        fun newInstance(mode: Mode): DrawingFragment {
            return DrawingFragment().apply {
                arguments = Bundle().apply {
                    putString("mode", mode.name)
                }
            }
        }
    }
}