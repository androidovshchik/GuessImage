package com.mygdx.guessimage.screen.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mygdx.guessimage.local.entities.PuzzleEntity

class PlayModel : ViewModel() {

    lateinit var puzzle: PuzzleEntity

    val framesGuessed = MutableLiveData<LongArray>()
}