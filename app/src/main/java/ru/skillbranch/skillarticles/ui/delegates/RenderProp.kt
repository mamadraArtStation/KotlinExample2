package ru.skillbranch.skillarticles.ui.delegates

import ru.skillbranch.skillarticles.ui.base.Binding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Отрисовка property
class RenderProp<T>(
    var value: T,
    needInit: Boolean = true, // Необходимость вызова обработчкика при инициализации
    private val onChange: ((T) -> Unit)? = null // Обработчик
): ReadWriteProperty<Binding, T> {
    private val listeners: MutableList<()->Unit> = mutableListOf()

    init{
        if (needInit) onChange?.invoke(value) // Если необходимо вызвать при инициализации
    }

    override fun getValue(thisRef: Binding, property: KProperty<*>): T = value
    override fun setValue(thisRef: Binding, property: KProperty<*>, value: T) {
        if(value == this.value) return
        this.value = value
        onChange?.invoke(this.value)
        if(listeners.isNotEmpty())
            listeners.forEach{it.invoke()}
    }

    // РЕгистрация доп listener-а
    fun addListener(listener:()->Unit) {
        listeners.add(listener)
    }
}

// Observe свойств
class ObserveProp<T: Any>(private val value: T, private val onChange: ((T) -> Unit)? = null) {
    // При создании делегата
    operator fun provideDelegate(thisRef: Binding, prop: KProperty<*>): ReadWriteProperty<Binding, T> {
        val delegate = RenderProp(value, true, onChange)
        registerDelegate(thisRef, prop.name, delegate)
        return delegate
    }
    // name - название свойства, delegate - привязанный к нему делегат
    private fun registerDelegate(thisRef: Binding, name: String, delegate: RenderProp<T>) {
        thisRef.delegates[name] = delegate
    }
}