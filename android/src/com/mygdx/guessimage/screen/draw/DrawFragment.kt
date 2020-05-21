package com.mygdx.guessimage.screen.draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.GuessImage
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.local.FileManager
import com.mygdx.guessimage.screen.edit.EditModel
import com.mygdx.guessimage.screen.play.PlayModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@Suppress("MemberVisibilityCanBePrivate")
class DrawFragment : AndroidFragmentApplication(), KodeinAware {

    override val kodein by closestKodein()

    private val fileManager by instance<FileManager>()

    private lateinit var drawModel: DrawModel

    lateinit var guessImage: GuessImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mode = Mode.valueOf(arguments!!.getString("mode")!!)
        val provider = ViewModelProvider(requireActivity())
        drawModel = if (mode == Mode.EDIT) {
            provider.get(EditModel::class.java)
        } else {
            provider.get(PlayModel::class.java)
        }
        var filename = arguments!!.getString("filename")
        if (!filename.isNullOrBlank()) {
            filename = fileManager.getImageFile(filename).path
        }
        guessImage = GuessImage(mode, filename, GuessImage.Listener {
            drawModel.frameChanged.postValue(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return initializeForView(guessImage)
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