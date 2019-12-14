package com.revolut.currencies.base.presentation.view.extension

import androidx.viewpager.widget.ViewPager

fun ViewPager.onPageSelected(pageSelected: (Int) -> Unit) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            pageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    })
}


