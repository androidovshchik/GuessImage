package com.mygdx.guessimage.screen.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.DummyHolder
import com.mygdx.guessimage.screen.PuzzleViewHolder
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance
import splitties.views.dsl.core.R
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.recyclerview.recyclerView

class ObjectsFragment : BaseFragment() {

    private val db by instance<Database>()

    private val dataSource = dataSourceOf("")

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return with(activity) {
            recyclerView {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                setHasFixedSize(true)
                setup {
                    withLayoutManager(LinearLayoutManager(context))
                    withDataSource(dataSource)
                    withItem<String, DummyHolder>(R.layout.item_new) {
                        onBind(::DummyHolder) { _, _ ->
                        }
                        onClick { index ->
                        }
                    }
                    withItem<PuzzleEntity, PuzzleViewHolder>(R.layout.item_puzzle) {
                        onBind(::PuzzleViewHolder) { index, item ->
                            pictogram.load(item.path)
                            count.text = "10"
                        }
                        onClick { index ->
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> launch {
                    withContext(Dispatchers.IO) {
                        presenter.getGalleryPath(applicationContext, data?.data ?: return)
                    }
                }
            }
        }
    }
}