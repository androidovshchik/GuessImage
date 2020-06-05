package com.mygdx.guessimage.screen.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.mygdx.guessimage.R
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import com.mygdx.guessimage.screen.play.ui.ListFragmentUI
import org.jetbrains.anko.AnkoContext

class ListFragment : BaseFragment() {

    private lateinit var playModel: PlayModel

    val dataSource = emptyDataSourceTyped<ObjectEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playModel = ViewModelProvider(requireActivity()).get(PlayModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return ListFragmentUI().createView(AnkoContext.create(requireActivity(), this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playModel.framesGuessed.observe(viewLifecycleOwner, Observer { ids ->
            var guessedCount = 0
            dataSource.toList().forEach {
                if (it.id in ids) {
                    it.isGuessed = true
                }
                if (it.isGuessed) {
                    guessedCount++
                }
            }
            dataSource.invalidateAll()
            if (guessedCount >= dataSource.size()) {
                AlertDialog.Builder(activity ?: return@Observer)
                    .setTitle(R.string.win)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        })
    }

    fun setObjects(items: List<ObjectEntity>) {
        dataSource.apply {
            clear()
            addAll(items)
            invalidateAll()
        }
    }

    companion object {

        val TAG = ListFragment::class.java.simpleName

        fun newInstance(): ListFragment {
            return ListFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}