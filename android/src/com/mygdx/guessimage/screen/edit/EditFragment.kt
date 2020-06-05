package com.mygdx.guessimage.screen.edit

import android.Manifest
import android.content.Intent
import android.os.Bundle
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
import com.mygdx.guessimage.screen.edit.ui.EditFragmentUI
import org.jetbrains.anko.AnkoContext

class EditFragment : BaseFragment() {

    private lateinit var editModel: EditModel

    private lateinit var drawFragment: DrawFragment

    var buttonSelect: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editModel = ViewModelProvider(requireActivity()).get(EditModel::class.java)
        drawFragment = DrawFragment.newInstance(Mode.EDIT, null)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return EditFragmentUI().createView(AnkoContext.create(requireActivity(), this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.transact {
            add(EditFragmentUI.idDrawing, drawFragment, DrawFragment.TAG)
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

    fun openPicker() {
        val activity = activity ?: return
        if (activity.areGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            activity.startActivityForResult(Intent.createChooser(Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }, getString(R.string.choose)), REQUEST_IMAGE)
        } else if (isMarshmallowPlus()) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE)
        }
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