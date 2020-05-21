package com.mygdx.guessimage.screen.draw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.model.Frame

open class DrawModel : ViewModel() {

    lateinit var puzzle: PuzzleEntity

    val frameChanged = MutableLiveData<Frame>()
}