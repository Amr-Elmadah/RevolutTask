package com.revolut.currencies.base.presentation.view.extension

import kotlin.math.roundToInt

fun roundToHalf(number: Double): Double = (number * 2).roundToInt() / 2.0