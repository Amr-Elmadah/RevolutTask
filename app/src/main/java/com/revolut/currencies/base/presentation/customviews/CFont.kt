package com.revolut.currencies.base.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.revolut.currencies.R
import com.revolut.currencies.application.BaseApp

object CFont {
    fun init(textView: TextView, context: Context, attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextFonts,
            0, 0
        )
        try {
            val mFontStyle = typedArray.getInteger(R.styleable.TextFonts_textStyleFont, 0)
            textView.typeface =
                when (mFontStyle) {
                    1 -> {
                        BaseApp.mediumFont
                    }
                    else -> {
                        BaseApp.regularFont
                    }
                }
        } finally {
            typedArray.recycle()
        }
    }
}
