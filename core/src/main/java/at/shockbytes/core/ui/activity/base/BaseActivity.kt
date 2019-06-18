package at.shockbytes.core.ui.activity.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.transition.Explode
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import at.shockbytes.core.ShockbytesApp
import at.shockbytes.core.ShockbytesInjector
import io.reactivex.disposables.CompositeDisposable

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<T: ShockbytesInjector> : AppCompatActivity() {

    open val enableActivityTransition: Boolean = true

    protected val compositeDisposable: CompositeDisposable =  CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (enableActivityTransition) {
                window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
                window.exitTransition = Slide(Gravity.BOTTOM)
                window.enterTransition = Explode()
            }
        }
        injectToGraph((application as? ShockbytesApp<T>)?.appComponent)
    }

    override fun onPause() {
        unbindViewModel()
        compositeDisposable.clear()
        super.onPause()
    }

    override fun onResume() {
        bindViewModel()
        super.onResume()
    }

    protected abstract fun bindViewModel()

    protected abstract fun unbindViewModel()

    protected abstract fun injectToGraph(appComponent: T?)

    protected fun showSnackbar(text: String) {
        if (!text.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show()
        }
    }

    protected fun showToast(text: Int, showLong: Boolean = false) {
        showToast(getString(text), showLong)
    }

    protected fun showToast(text: String, showLong: Boolean = false) {
        val length = if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(applicationContext, text, length).show()
    }

    fun startActivityDelayed(intent: Intent, bundle: Bundle?, delay: Long) {
        Handler().postDelayed ( {
            startActivity(intent, bundle)
        }, delay)
    }

}
