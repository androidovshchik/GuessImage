package com.mygdx.guessimage.screen.editor

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mygdx.guessimage.PathCompat
import com.mygdx.guessimage.R
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.kodein.di.generic.instance

class ObjectFragment : BaseFragment() {

    private val db by instance<Database>()

    @Suppress("DEPRECATION")
    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            frameLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                setBackgroundColor(Color.parseColor("#E1BEE7"))
                imageView {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }.lparams(matchParent, matchParent)
                button {
                    text = getString(R.string.upload)
                    setOnClickListener {
                        startActivityForResult(Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_GET_CONTENT
                            type = "image/*"
                        }, getString(R.string.choose)), REQUEST_IMAGE)
                    }
                }.lparams(wrapContent, wrapContent, Gravity.CENTER)
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE -> {
                    launch {
                        withContext(Dispatchers.IO) {
                            PathCompat.getFilePath(, data?.data ?: return)
                        }
                    }
                }
            }
        }
    }

    companion object {

        const val REQUEST_IMAGE = 100
    }
}