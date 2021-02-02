package ru.skillbranch.skillarticles.viewmodels.base

import android.os.Bundle

interface IViewModelState { // Просто операции с bundle
    fun save(outState: Bundle)
    fun restore(savedState: Bundle) : IViewModelState
}