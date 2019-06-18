package at.shockbytes.core.ui.fragment.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import at.shockbytes.core.ShockbytesApp
import at.shockbytes.core.ShockbytesInjector
import io.reactivex.disposables.CompositeDisposable

/**
 * Author:  Martin Macheiner
 * Date:    30.12.2017
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseDialogFragment<T: ShockbytesInjector>: DialogFragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectToGraph((activity?.application as? ShockbytesApp<T>)?.appComponent)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    abstract fun injectToGraph(appComponent: T?)

}