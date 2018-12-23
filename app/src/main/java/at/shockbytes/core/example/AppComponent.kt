package at.shockbytes.core.example

import at.shockbytes.core.ShockbytesInjector

interface AppComponent: ShockbytesInjector {

    fun inject(activity: MainActivity)
}