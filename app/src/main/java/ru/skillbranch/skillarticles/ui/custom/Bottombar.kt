package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.shape.MaterialShapeDrawable
import ru.skillbranch.skillarticles.R

class Bottombar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAtr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAtr) {
    init {
        View.inflate(context, R.layout.layout_bottombar, this)
        //add material bg for handle elevation and color surface
        val materialBg = MaterialShapeDrawable.createWithElevationOverlay(context)
        materialBg.elevation = elevation
        background = materialBg
    }
}