package com.revolut.currencies.base.presentation.view.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun FragmentManager.replaceFragment(
    activity: AppCompatActivity, fragment: Fragment,
    frameId: Int, addToBackStack: Boolean = false, tag: String = ""
) {
    activity.supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        takeIf { addToBackStack }?.apply { addToBackStack(tag) }
    }
}