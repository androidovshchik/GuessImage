package com.mygdx.guessimage

import android.os.Bundle
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleEntity
import org.kodein.di.generic.instance
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.recyclerview.recyclerView

class MainActivity : BaseActivity() {

    private val db by instance<Database>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataSource = dataSourceTypedOf(
            Person("Aidan", 24),
            Person("Nina", 24)
        )
        setContentView(frameLayout {
            recyclerView {
                setup {
                    withDataSource(dataSource)
                    withItem<PuzzleEntity, PersonViewHolder>(R.layout.person_item_layout) {
                        onBind(::PersonViewHolder) { index, item ->
                            // PersonViewHolder is `this` here
                            name.text = item.name
                            age.text = "${item.age}"
                        }
                        onClick { index ->

                        }
                    }
                }
            }
        })
    }
}
