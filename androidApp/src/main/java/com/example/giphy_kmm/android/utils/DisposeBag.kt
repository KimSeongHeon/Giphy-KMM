package com.example.giphy_kmm.android.utils

import androidx.lifecycle.LifecycleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.HashMap

class DisposeBag: LifecycleObserver {

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var exclusiveDisposableMap: MutableMap<String, Disposable>

    private var valid = true

    init { reset() }

    private fun reset() {
        compositeDisposable = CompositeDisposable()
        exclusiveDisposableMap = HashMap()
    }

    private fun add(disposable: Disposable?) {
        if (disposable == null) {
            return
        }
        if (!isValid()) {
            disposable.dispose()
            return
        }
        compositeDisposable.add(disposable)
    }

    fun add(vararg disposables: Disposable?) {
        for (disposable in disposables) {
            add(disposable)
        }
    }

    fun dispose() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        reset()
        valid = false
    }

    fun clear() {
        dispose()
        setValid(true)
    }

    protected fun setValid(valid: Boolean) {
        this.valid = valid
    }

    private fun isValid(): Boolean {
        return valid
    }



    fun addExclusive(disposable: Disposable, tag: String) {
        if (!isValid()) {
            disposable.dispose()
            return
        }
        registerExclusiveDisposable(disposable, tag)
    }

    fun disposeExclusiveDisposalByTag(tag: String) {
        if (exclusiveDisposableMap.containsKey(tag)) {
            val prevDisposable = exclusiveDisposableMap.remove(tag)
            compositeDisposable.remove(prevDisposable!!)
            if (!prevDisposable.isDisposed) {
                prevDisposable.dispose()
            }
        }
    }

    private fun registerExclusiveDisposable(disposable: Disposable, tag: String) {
        if (exclusiveDisposableMap.containsKey(tag)) {
            val prevDisposable = exclusiveDisposableMap.remove(tag)
            compositeDisposable.remove(prevDisposable!!)
            if (!prevDisposable.isDisposed) {
                prevDisposable.dispose()
            }
        }
        exclusiveDisposableMap[tag] = disposable
        compositeDisposable.add(disposable)
    }

    companion object {
        private const val DEFAULT_EXCLUSIVE_TAG = "defaultExclusive"
    }
}
