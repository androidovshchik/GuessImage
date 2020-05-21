package com.mygdx.guessimage.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.GuessImage
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.local.FileManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@Suppress("MemberVisibilityCanBePrivate")
class DrawingFragment : AndroidFragmentApplication(), KodeinAware {

    override val kodein by closestKodein()

    private val fileManager by instance<FileManager>()

    lateinit var guessImage: GuessImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mode = Mode.valueOf(arguments!!.getString("mode")!!)
        var filename = arguments!!.getString("filename")
        if (!filename.isNullOrBlank()) {
            filename = fileManager.getImageFile(filename).path
        }
        guessImage = GuessImage(mode, filename, GuessImage.Listener {

        })
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return initializeForView(guessImage)
    }

    companion object {

        val TAG = DrawingFragment::class.java.simpleName

        fun newInstance(mode: Mode, filename: String?): DrawingFragment {
            return DrawingFragment().apply {
                arguments = Bundle().apply {
                    putString("mode", mode.name)
                    putString("filename", filename)
                }
            }
        }
    }
}