package ru.skillbranch.skillarticles.ui.custom

import android.text.TextPaint

// Подкрашиваем результат поиска
class SearchFocusSpan(private val bgColor: Int, private val fgColor:Int) : SearchSpan(bgColor, fgColor) {

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.bgColor = bgColor
        textPaint.color = fgColor
    }
}