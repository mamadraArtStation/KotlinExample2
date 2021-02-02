package ru.skillbranch.skillarticles.data.delegates

import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//Реализуй делегат PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> (ru.skillbranch.skillarticles.data.delegates.PrefDelegate)
// возвращающий значений примитивов (Boolean, String, Float, Int, Long)
//
//Пример: var storedBoolean by PrefDelegate(false)
//var storedString by PrefDelegate("")
//var storedFloat by PrefDelegate(0f)
//var storedInt by PrefDelegate(0)
//var storedLong by PrefDelegate(0)
//
//Реализуй в классе PrefManager(context:Context) (ru.skillbranch.skillarticles.data.local.PrefManager)
//свойство val preferences : SharedPreferences проинициализированое экземпляром SharedPreferences приложения.
//И метод fun clearAll() - очищающий все сохраненные значения SharedPreferences приложения.
//Использовать PrefManager из androidx (import androidx.preference.PreferenceManager)

//class PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> {
//    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
//        if  (thisRef.preferences.all[property.name] == null)
//            setValue(thisRef, property, defaultValue)
//        return thisRef.preferences.all[property.name] as T?
//
//    }
//
//    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
//        val valueR:T = value?: defaultValue
//        with(thisRef.preferences.edit()) {
//            when (valueR) {
//                is Boolean -> putBoolean(property.name, valueR).apply()
//                is Float -> putFloat(property.name, valueR).apply()
//                is Long -> putLong(property.name, valueR).apply()
//                is Int -> putInt(property.name, valueR).apply()
//                is String -> putString(property.name, valueR).apply()
//                else -> throw NotImplementedError("Wrong type of value. Only Boolean, Float, Long, Int, String types are possible!")
//            }
//        }
//    }

class PrefDelegate<T>(private val defaultValue: T) {
    private var storedValue: T? = null
    operator fun provideDelegate(thisRef: PrefManager, property: KProperty<*>): ReadWriteProperty<PrefManager, T?> {
        val key = property.name
        return object : ReadWriteProperty<PrefManager, T?> {
            override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
                if (storedValue == null) {
                    @Suppress("UNCHECKED_CAST")
                    storedValue = when(defaultValue) {
                        is Boolean -> thisRef.preferences.getBoolean(key, defaultValue as Boolean) as T
                        is Float -> thisRef.preferences.getFloat(key, defaultValue as Float) as T
                        is Long -> thisRef.preferences.getLong(key, defaultValue as Long) as T
                        is Int -> thisRef.preferences.getInt(key, defaultValue as Int) as T
                        is String -> thisRef.preferences.getString(key, defaultValue as String) as T
                        else ->  error("Wrong type of value. Only Boolean, Float, Long, Int, String types are possible!")
                    }
                }
                return storedValue
            }

            override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
                with(thisRef.preferences.edit()) {
                    when(value)  {
                        is Boolean -> putBoolean(key,value)
                        is Float ->  putFloat(key,value)
                        is Long ->  putLong(key,value)
                        is Int ->  putInt(key,value)
                        is String ->  putString(key,value)
                        else ->  error("Wrong type of value. Only Boolean, Float, Long, Int, String types are possible!")
                    }
                    apply()
                }
                storedValue = value
            }

        }

    }

}