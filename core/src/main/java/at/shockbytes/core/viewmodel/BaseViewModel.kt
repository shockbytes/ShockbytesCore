package at.shockbytes.core.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Author:  Martin Macheiner
 * Date:    12.06.2018
 */
abstract class BaseViewModel: ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}