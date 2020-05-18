package com.mygdx.guessimage.screen.base

import android.view.WindowManager
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import timber.log.Timber

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment : Fragment(), KodeinAware, CoroutineScope {

    override val kodein by closestKodein()

    protected val job = SupervisorJob()

    var isTouchable
        get() = activity?.window?.attributes?.flags?.and(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) == 0
        set(value) {
            val flag = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            if (value) {
                activity?.window?.clearFlags(flag)
            } else {
                activity?.window?.setFlags(flag, flag)
            }
        }

    override fun onDestroyView() {
        job.cancelChildren()
        super.onDestroyView()
    }

    override val coroutineContext = Dispatchers.Main + job + CoroutineExceptionHandler { _, e ->
        Timber.e(e)
    }
}