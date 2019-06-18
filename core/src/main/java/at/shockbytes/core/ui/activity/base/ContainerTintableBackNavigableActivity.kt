package at.shockbytes.core.ui.activity.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import at.shockbytes.core.ShockbytesInjector

/**
 * Author:  Martin Macheiner
 * Date:    23.12.2017
 */
abstract class ContainerTintableBackNavigableActivity<T : ShockbytesInjector> : TintableBackNavigableActivity<T>() {

    abstract val displayFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, displayFragment)
            .commit()
    }

}