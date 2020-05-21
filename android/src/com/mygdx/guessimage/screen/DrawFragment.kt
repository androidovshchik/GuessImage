package com.mygdx.guessimage.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.GuessImage
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.local.FileManager
import com.mygdx.guessimage.local.entities.ObjectData
import com.mygdx.guessimage.screen.edit.EditModel
import com.mygdx.guessimage.screen.play.PlayModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@Suppress("MemberVisibilityCanBePrivate")
class DrawFragment : AndroidFragmentApplication(), KodeinAware, GuessImage.Listener {

    override val kodein by closestKodein()

    private val fileManager by instance<FileManager>()

    private lateinit var viewModel: ViewModel

    lateinit var guessImage: GuessImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mode = Mode.valueOf(arguments!!.getString("mode")!!)
        val provider = ViewModelProvider(requireActivity())
        viewModel = if (mode == Mode.EDIT) {
            provider.get(EditModel::class.java)
        } else {
            provider.get(PlayModel::class.java)
        }
        var filename = arguments!!.getString("filename")
        if (!filename.isNullOrBlank()) {
            filename = fileManager.getImageFile(filename).path
        }
        guessImage = GuessImage(mode, filename, this)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return initializeForView(guessImage)
    }

    override fun onFrameChanged(id: Long, x0: Float, y0: Float, width: Float, height: Float) {
        if (viewModel is EditModel) {
            (viewModel as EditModel).frameChanged.postValue(ObjectData(id, x0, y0, width, height))
        }
    }

    override fun onFramesGuessed(ids: List<Long>) {
        if (viewModel is PlayModel) {
            (viewModel as PlayModel).framesGuessed.postValue(ids)
        }
    }

    companion object {

        val TAG = DrawFragment::class.java.simpleName

        fun newInstance(mode: Mode, filename: String?): DrawFragment {
            return DrawFragment().apply {
                arguments = Bundle().apply {
                    putString("mode", mode.name)
                    putString("filename", filename)
                }
            }
        }
    }
}