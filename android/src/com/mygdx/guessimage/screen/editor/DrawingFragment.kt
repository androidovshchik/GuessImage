package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.BuildConfig
import com.mygdx.guessimage.GuessImage
import com.mygdx.guessimage.R

class DrawingFragment : AndroidFragmentApplication() {

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        val worldWidth: Int = getResources().getDimensionPixelSize(R.dimen.world_width)
        world = GuessImage(
            BuildConfig.DEBUG,
            worldWidth,
            worldWidth,
            object : StickersPressListener() {
                fun onStickerLongPress() {
                    EventUtil.post(StickerPressEvent())
                }
            })
        return initializeForView(world)
    }
}