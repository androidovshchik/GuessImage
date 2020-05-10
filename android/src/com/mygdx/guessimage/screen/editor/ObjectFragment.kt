package com.mygdx.guessimage.screen.editor

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
                        activity?.startActivityForResult(Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_GET_CONTENT
                            type = "image/*"
                        }, getString(R.string.choose)), REQUEST_IMAGE)
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
        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            when (requestCode) {
                REQUEST_IMAGE -> {
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

    companion object {

        const val REQUEST_IMAGE = 100

        fun newInstance(obj: ObjectEntity): ObjectFragment {
            return ObjectFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("object", obj)
                }
            }
        }
    }
}