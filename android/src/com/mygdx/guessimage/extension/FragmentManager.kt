@file:Suppress("DEPRECATION")

package com.mygdx.guessimage.extension

import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction

val FragmentManager.topFragment: Fragment?
    get() = findFragmentByTag((backStackEntryCount - 1).toString())

fun FragmentManager.showFragment(id: Int) {
    findFragmentById(id)?.let {
        transact {
            show(it)
        }
    }
}

fun FragmentManager.hideFragment(id: Int) {
    findFragmentById(id)?.let {
        transact {
            hide(it)
        }
    }
}

fun FragmentManager.addFragment(id: Int, fragment: Fragment) {
    transact {
        add(id, fragment, backStackEntryCount.toString())
        addToBackStack(fragment.javaClass.name)
    }
}

fun FragmentManager.putFragment(id: Int, fragment: Fragment) {
    transact {
        replace(id, fragment, backStackEntryCount.toString())
        addToBackStack(fragment.javaClass.name)
    }
}

inline fun FragmentManager.transact(
    commit: Boolean = true,
    action: FragmentTransaction.() -> Unit
) {
    beginTransaction().apply {
        action()
        if (commit) {
            commitAllowingStateLoss()
            executePendingTransactions()
        }
    }
}

fun FragmentManager.popFragment(name: String?, immediate: Boolean): Boolean {
    return if (name != null) {
        if (immediate) {
            popBackStackImmediate(name, 0)
        } else {
            popBackStack(name, 0)
            true
        }
    } else {
        if (immediate) {
            popBackStackImmediate()
        } else {
            popBackStack()
            true
        }
    }
}