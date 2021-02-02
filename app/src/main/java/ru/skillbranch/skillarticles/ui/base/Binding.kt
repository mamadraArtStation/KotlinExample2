package ru.skillbranch.skillarticles.ui.base

import android.os.Bundle
import ru.skillbranch.skillarticles.ui.delegates.RenderProp
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import kotlin.reflect.KProperty

// Связывание данных и view
abstract class Binding {
    val delegates = mutableMapOf<String, RenderProp<out Any>>() // название свойств и сам делегат

    abstract fun onFinishInflate()
    abstract fun bind(data: IViewModelState)
    abstract fun saveUi(outState: Bundle)
    abstract fun restoreUi(savedState: Bundle)

    // Наблюдаем за 4мя полями
    @Suppress("UNCHECKED_CAST")
    fun <A, B, C, D> dependsOn(
        vararg fields: KProperty<*>, // Наблюдаемый массив (isLike, isBookmark и т.п.)
        onChange: (A, B, C, D) -> Unit // Обработчик при изменении одного из этих свойств
    ) {
        check(fields.size == 4) {"Names size must be 4, current ${fields.size}"}
        val names = fields.map { it.name }

        names.forEach {// Перебираем имена
            delegates[it]?.addListener { // если есть делагат - вызываем у него addListener
                onChange( // если свойство делегата изменено - вызываем onChange с 4мя аргументами
                    delegates[names[0]]?.value as A,
                    delegates[names[1]]?.value as B,
                    delegates[names[2]]?.value as C,
                    delegates[names[3]]?.value as D
                )
            }
        }
    }

}