package com.example.giphy_kmm.android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.giphy_kmm.android.utils.DisposeBag
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    private val disposeBag = DisposeBag()
    private val DEFAULT_EXCLUSIVE_TAG = "defaultExclusive"

    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }

    protected fun addDisposable(disposable: Disposable?) {
        if (disposable == null) return
        disposeBag.add(disposable)
    }


    protected fun addExclusive(disposable: Disposable, tag: String = DEFAULT_EXCLUSIVE_TAG) {
        disposeBag.addExclusive(disposable, tag)
    }

    protected fun disposeExclusiveByTag(tag: String) {
        disposeBag.disposeExclusiveDisposalByTag(tag = tag)
    }
}
