package com.mygdx.guessimage.screen.editor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.api.load
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.isVisible
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import org.jetbrains.anko.*

@Suppress("DEPRECATION")
class ObjectFragment : BaseFragment() {

    private lateinit var obj: ObjectEntity

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obj = arguments.getSerializable("object") as ObjectEntity
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        val uri = obj.uri
        return UI {
            frameLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                button {
                    text = getString(R.string.upload)
                    isVisible = uri == null
                    setOnClickListener {
                        activity?.startActivityForResult(Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_GET_CONTENT
                            type = "image/*"
                        }, getString(R.string.choose)), REQUEST_IMAGE)
                    }
                }.lparams(wrapContent, wrapContent, Gravity.CENTER)
                image = imageView {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    isVisible = uri != null
                    load(uri)
                }.lparams(matchParent, matchParent)
            }
        }.view
    }

    companion object {

        const val REQUEST_IMAGE = 100
    }
}