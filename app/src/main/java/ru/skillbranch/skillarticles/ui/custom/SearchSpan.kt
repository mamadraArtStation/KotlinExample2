package ru.skillbranch.skillarticles.ui.custom

import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import androidx.core.graphics.ColorUtils

// Span - фреймворк для отрисовки текста, когда применим его к TextView он применит цвета на часть текста
// open - чтобы можно было унаследоваться
open class SearchSpan(bgColor: Int, private val fgColor: Int) : BackgroundColorSpan(bgColor) {
    private val alphaColor by lazy {
        ColorUtils.setAlphaComponent(backgroundColor, 160) // Полупрозрачный цвет
    }

    // textPaint-ом отрисовывается текст
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.bgColor = alphaColor
        textPaint.color = fgColor // цвет букв
    }
}