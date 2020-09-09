package ru.skillbranch.skillarticles.viewmodels

import androidx.annotation.UiThread
import androidx.lifecycle.*

abstract class BaseViewModel<T>(initState: T) : ViewModel() {
    val notifications = MutableLiveData<Event<Notify>>()
    val state: MediatorLiveData<T> = MediatorLiveData<T>().apply {
        value = initState
    }

    protected val currentState
        get() = state.value!!

    @UiThread
    protected inline fun updateState(update: (currentState: T) -> T){
        val updateState: T = update(currentState)
        state.value = updateState
    }

    @UiThread
    protected fun notify(content: Notify) {
        notifications.value = Event(content)
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit) {
        state.observe(owner, Observer{onChanged(it!!)})
    }

    fun observeNotifications(owner: LifecycleOwner, onNotify: (notifications: Notify) -> Unit) {
        notifications.observe(owner, EventObserver{onNotify(it)})
    }

    protected fun <S> subscribeOnDataSource(source: LiveData<S>, onChanged: (newValue: S, currentState: T) -> T?) {
        state.addSource(source) {
            state.value = onChanged(it, currentState) ?: return@addSource
        }
    }
}

class ViewModelFactory(private val params: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(params) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class Event<out E>(private val content: E) {
    var hasBeenHandled = false

    fun getContentIfNoHandled(): E? {
        return if(hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): E {
        return content
    }
}

class EventObserver<E>(private val onEventUnhandledContent: (E) -> Unit): Observer<Event<E>> {
    override fun onChanged(t: Event<E>?) {
        t?.getContentIfNoHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

sealed class Notify(val message: String) {
    data class TextMessage(val msg: String) : Notify(msg)

    data class ActionMessage(
        val msg: String,
        val actionLabel: String,
        val actionHandler: (() -> Unit)?
    ) : Notify(msg)

    data class ErrorMessage(
        val msg: String,
        val errLabel: String,
        val errHandler: (() -> Unit)?
    ) : Notify(msg)
}