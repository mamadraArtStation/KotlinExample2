package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

//Реализуй функцию расширения View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop,
// right : Int = marginRight, bottom : Int = marginBottom), в качестве аргумента принимает значения
// внешних отступов View (margin) в пикселях
fun View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop, right : Int = marginRight, bottom : Int = marginBottom) {
    val layoutParams = (this.layoutParams  as? ViewGroup.MarginLayoutParams)
    layoutParams ?.bottomMargin = bottom
    layoutParams ?.topMargin = top
    layoutParams ?.rightMargin = right
    layoutParams ?.leftMargin = left
}