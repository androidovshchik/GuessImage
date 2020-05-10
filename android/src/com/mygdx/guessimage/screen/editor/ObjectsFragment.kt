package com.mygdx.guessimage.screen.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.PathCompat
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.activityCallback
import com.mygdx.guessimage.extension.nestedScrollView
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.android.synthetic.main.item_object.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.kodein.di.generic.instance

class ObjectViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.tv_name
}

@Suppress("DEPRECATION")
class ObjectsFragment : BaseFragment() {

    private val db by instance<Database>()

    private val dataSource = dataSourceOf()

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            verticalLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                button {
                    text = getString(R.string.btn_close)
                    setOnClickListener {
                        activity?.finish()
                    }
                }.lparams(matchParent, wrapContent)
                nestedScrollView {
                    verticalLayout {
                        layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
                        recyclerView {
                            isNestedScrollingEnabled = false
                            setup {
                                withLayoutManager(LinearLayoutManager(context))
                                withDataSource(dataSource)
                                withItem<ObjectEntity, ObjectViewHolder>(R.layout.item_object) {
                                    onBind(::ObjectViewHolder) { index, item ->
                                    }
                                    onClick { index ->
                                    }
                                }
                            }
                        }.lparams(matchParent, wrapContent)
                        button {
                            text = getString(R.string.btn_add)
                            setOnClickListener {
                                appContext?.activityCallback<EditorActivity> {
                                    editObject(ObjectEntity())
                                }
                            }
                        }.lparams(matchParent, wrapContent)
                    }
                }.lparams(matchParent, 0, 1f)
                button {
                    text = getString(R.string.btn_save)
                    setOnClickListener {

                    }
                }.lparams(matchParent, wrapContent)
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            val items = withContext(Dispatchers.IO) {
                db.objectDao().getAll()
            }
            dataSource.apply {
                clear()
                invalidateAll()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ObjectFragment.REQUEST_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data ?: return
                    launch {
                        val path = withContext(Dispatchers.IO) {
                            PathCompat.getFilePath(appContext!!, uri)
                        }
                        //image.load(path)
                    }
                }
            }
        }
    }

    companion object {

        val TAG = ObjectsFragment::class.java.simpleName

        fun newInstance(): ObjectsFragment {
            return ObjectsFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}