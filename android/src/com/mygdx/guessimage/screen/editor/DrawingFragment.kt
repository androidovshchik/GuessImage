package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.GuessImage
import com.mygdx.guessimage.local.FileManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@Suppress("MemberVisibilityCanBePrivate")
class DrawingFragment : AndroidFragmentApplication(), KodeinAware {

    override val kodein by closestKodein()

    private val fileManager by instance<FileManager>()

    private lateinit var puzzleModel: PuzzleModel

    lateinit var guessImage: GuessImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        puzzleModel = ViewModelProvider(requireActivity()).get(PuzzleModel::class.java)
        var filename = puzzleModel.puzzle.filename
        if (!filename.isNullOrBlank()) {
            filename = fileManager.getImageFile(filename).path
        }
        guessImage = GuessImage(puzzleModel.mode, filename, object : GuessImage.Listener {
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