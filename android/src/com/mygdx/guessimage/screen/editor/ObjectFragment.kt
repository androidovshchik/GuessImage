package com.mygdx.guessimage.screen.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.recyclical.datasource.dataSourceOf
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.screen.base.BaseFragment
import org.kodein.di.generic.instance
import splitties.views.dsl.core.*
import splitties.views.onClick

class ObjectFragment : BaseFragment() {

    private val db by instance<Database>()

    private val dataSource = dataSourceOf("")

    @Suppress("DEPRECATION")
    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return with(activity) {
            frameLayout {
                lParams(matchParent, matchParent)
                imageView {
                    lParams(matchParent, matchParent)
                    //background = Color.BLUE
                }
                addView(button {
                    text = "Выберите приложение"
                    onClick {
                        startActivityForResult(Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_GET_CONTENT
                            type = "image/*"
                        }, "Выберите приложение"), 100)
                    }
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                /*100 -> launch {
                    withContext(Dispatchers.IO) {
                        presenter.getGalleryPath(applicationContext, data?.data ?: return)
                    }
                }*/
            }
        }
    }
}