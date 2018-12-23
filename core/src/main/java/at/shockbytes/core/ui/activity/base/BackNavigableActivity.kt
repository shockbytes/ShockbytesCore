package at.shockbytes.core.ui.activity.base

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.view.MenuItem
import at.shockbytes.core.ShockbytesInjector

abstract class BackNavigableActivity<T: ShockbytesInjector> : BaseActivity<T>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            back()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        back()
    }

    private fun back() {
        backwardAnimation()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            onBackStackPopped()
        } else {
            super.onBackPressed()
        }
    }

    open fun backwardAnimation() = Unit

    open fun onBackStackPopped() = Unit

    fun setHomeAsUpIndicator(@DrawableRes indicator: Int) {
        supportActionBar?.setHomeAsUpIndicator(indicator)
    }

}
