package com.mygdx.guessimage.screen.editor

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import coil.api.load
import com.mygdx.guessimage.PathCompat
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.areGranted
import com.mygdx.guessimage.extension.isMarshmallowPlus
import com.mygdx.guessimage.extension.isVisible
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*

@Suppress("DEPRECATION")
class ObjectFragment : BaseFragment() {

    private lateinit var obj: ObjectEntity

    private lateinit var button: Button

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obj = arguments.getSerializable("object") as ObjectEntity
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            frameLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                button = button {
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
                image = imageView {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }.lparams(matchParent, matchParent)
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notifyPath(obj.uri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data ?: return
                    launch {
                        val path = withContext(Dispatchers.IO) {
                            PathCompat.getFilePath(appContext!!, uri)
                        }
                        notifyPath(path)
                    }
                }
            }
        }
    }

    private fun notifyPath(path: String?) {
        button.isVisible = path == null
        image.apply {
            isVisible = path != null
            load(path)
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

        fun newInstance(obj: ObjectEntity): ObjectFragment {
            return ObjectFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("object", obj)
                }
            }
        }
    }
}