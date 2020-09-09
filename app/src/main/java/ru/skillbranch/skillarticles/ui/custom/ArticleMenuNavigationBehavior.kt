package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import ru.skillbranch.skillarticles.R
import kotlin.math.max
import kotlin.math.min

class ArticleMenuNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    private val actionBarSize: Float

    init {
        val styledAttributes: TypedArray =
            context.theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        actionBarSize = styledAttributes.getDimension(0, 0f)
        styledAttributes.recycle()
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        //we care about vertical scroll events
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        //clamps the translationY value between two bounds — 0 and the view height
        child.translationY = max(
            0f,
            min(child.height.toFloat() + actionBarSize, child.translationY + dy)
        )
    }
}