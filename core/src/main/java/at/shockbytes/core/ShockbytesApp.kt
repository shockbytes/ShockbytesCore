package at.shockbytes.core

import android.app.Application
import android.os.StrictMode
import android.support.v7.app.AppCompatDelegate
import at.shockbytes.core.logging.CrashlyticsReportingTree
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import io.fabric.sdk.android.Fabric
import timber.log.Timber

abstract class ShockbytesApp<T : ShockbytesInjector> : Application() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    lateinit var appComponent: T
        private set

    abstract val useCrashlytics: Boolean

    abstract val useStrictModeInDebug: Boolean

    override fun onCreate() {
        super.onCreate()

        if (useStrictModeInDebug) {
            setStrictMode()
        }

        initializeLibraries()

        configureCrashReporting()
        configureLogging()

        appComponent = setupInjectionAppComponent()
    }

    private fun configureLogging() {
        when {
            BuildConfig.DEBUG -> Timber.plant(Timber.DebugTree())
            useCrashlytics -> Timber.plant(CrashlyticsReportingTree())
            else -> setupCustomLogging()
        }
    }

    private fun configureCrashReporting() {

        if (useCrashlytics) {
            setupCrashlytics()
        }
    }

    private fun setupCrashlytics() {
        // Configure Crashlytics anyway
        Fabric.with(
            Fabric.Builder(this)
                .kits(Crashlytics(), Answers())
                .debuggable(BuildConfig.DEBUG)
                .build()
        )

        // to catch and send crash report to crashlytics when app crashes
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Timber.e(e, "uncaught exception")
            defaultExceptionHandler.uncaughtException(t, e)
        }
    }

    private fun setStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )

            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
    }

    /**
     * Initialize some custom libraries like JodaTime or Realm.
     */
    abstract fun initializeLibraries()

    /**
     * Setup here your Dagger app component module declared as the generic T
     */
    abstract fun setupInjectionAppComponent(): T

    /**
     * If you don't want to use Crashlytics as release crash reporter, then set your custom implementation here up.
     */
    abstract fun setupCustomLogging()

}
