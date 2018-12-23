package at.shockbytes.core.ui.activity.base

import android.os.Bundle
import android.support.v4.app.Fragment
import at.shockbytes.core.ShockbytesInjector

/**
 * Author:  Martin Macheiner
 * Date:    23.12.2017
 */
abstract class ContainerBackNavigableActivity<T: ShockbytesInjector> : BackNavigableActivity<T>() {

    abstract val displayFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, displayFragment)
            .commit()
    }

    override fun injectToGraph(appComponent: T?) {
        // Do nothing, not necessary here...
    }

}