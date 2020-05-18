package com.mygdx.guessimage.screen.editor

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.areGranted
import com.mygdx.guessimage.extension.isMarshmallowPlus
import com.mygdx.guessimage.extension.isVisible
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import org.jetbrains.anko.button
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.wrapContent

class ObjectFragment : BaseFragment() {

    private val idDrawing = View.generateViewId()

    private lateinit var puzzleModel: PuzzleModel

    private lateinit var button: Button

    private var currentObj: ObjectEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        puzzleModel = ViewModelProvider(requireActivity()).get(PuzzleModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            frameLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idDrawing
                }.lparams(matchParent, matchParent)
                button = button {
                    text = getString(R.string.upload)
                    isVisible =
                        puzzleModel.mode == Mode.EDIT && puzzleModel.puzzle.filename.isNullOrBlank()
                    setOnClickListener {
                        val activity = activity ?: return@setOnClickListener
                        if (activity.areGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            activity.startActivityForResult(Intent.createChooser(Intent().apply {
                                action = Intent.ACTION_GET_CONTENT
                                type = "image/*"
                            }, getString(R.string.choose)), REQUEST_IMAGE)
                        } else if (isMarshmallowPlus()) {
                            requestPermissions(
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE
                            )
                        }
                    }
                }.lparams(wrapContent, wrapContent, Gravity.CENTER)
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.transact {
            add(idDrawing, DrawingFragment.newInstance(), DrawingFragment.TAG)
        }
        puzzleModel.currentObj.observe(viewLifecycleOwner, Observer {
            currentObj = it
        })
        if (puzzleModel.mode == Mode.EDIT) {
            puzzleModel.galleryPath.observe(viewLifecycleOwner, Observer {
                button.isVisible = false
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, p: Array<out String>, r: IntArray) {
        when (requestCode) {
            REQUEST_STORAGE -> {
                button.performClick()
            }
        }
    }

    companion object {

        const val REQUEST_STORAGE = 1

        const val REQUEST_IMAGE = 100

        val TAG = ObjectFragment::class.java.simpleName

        fun newInstance(): ObjectFragment {
            return ObjectFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}