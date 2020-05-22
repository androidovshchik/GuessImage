package com.mygdx.guessimage.screen.edit

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
import com.mygdx.guessimage.screen.DrawFragment
import com.mygdx.guessimage.screen.base.BaseFragment
import org.jetbrains.anko.button
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.wrapContent

class EditFragment : BaseFragment() {

    private val idDrawing = View.generateViewId()

    private lateinit var editModel: EditModel

    private lateinit var drawFragment: DrawFragment

    private var buttonSelect: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editModel = ViewModelProvider(requireActivity()).get(EditModel::class.java)
        drawFragment = DrawFragment.newInstance(Mode.EDIT, null)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            frameLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idDrawing
                }.lparams(matchParent, matchParent)
                buttonSelect = button {
                    text = getString(R.string.upload)
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
            add(idDrawing, drawFragment, DrawFragment.TAG)
        }
        editModel.galleryPath.observe(viewLifecycleOwner, Observer {
            buttonSelect?.isVisible = false
            drawFragment.guessImage.postRunnable("setBackground", it)
        })
        editModel.newObject.observe(viewLifecycleOwner, Observer {
            drawFragment.guessImage.postRunnable("addFrame", it.id)
        })
        editModel.readyObject.observe(viewLifecycleOwner, Observer {
            drawFragment.guessImage.postRunnable("markFrame", it.id)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, p: Array<out String>, r: IntArray) {
        when (requestCode) {
            REQUEST_STORAGE -> {
                buttonSelect?.performClick()
            }
        }
    }

    companion object {

        const val REQUEST_STORAGE = 1

        const val REQUEST_IMAGE = 100

        val TAG = EditFragment::class.java.simpleName

        fun newInstance(): EditFragment {
            return EditFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}