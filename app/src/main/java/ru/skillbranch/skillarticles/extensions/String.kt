package ru.skillbranch.skillarticles.extensions

import java.util.*

// Реализуй функцию расширения fun String.indexesOf(substr: String, ignoreCase: Boolean = true): List,
// в качестве аргумента принимает подстроку и флаг - учитывать или нет регистр подстроки при поиске по исходной строке.
// Возвращает список позиций вхождений подстроки в исходную строку. Пример: "lorem ipsum sum".indexesOf("sum") // [8, 12]
fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    return if (this == null || substr.isEmpty()) listOf()
    else Regex(if (ignoreCase)substr.toLowerCase(Locale.getDefault()) else substr)
        .findAll(if (ignoreCase) this.toLowerCase(Locale.getDefault()) else this)
        .map { it.range.first }
        .toList()
}