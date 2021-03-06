package at.shockbytes.core.ui.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuBuilder
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import at.shockbytes.core.R
import at.shockbytes.core.ShockbytesInjector
import at.shockbytes.core.image.ImageLoader
import at.shockbytes.core.model.LoginUserEvent
import at.shockbytes.core.model.ShockbytesUser
import at.shockbytes.core.ui.model.BottomNavigationActivityOptions
import at.shockbytes.core.ui.activity.base.BaseActivity
import at.shockbytes.core.ui.model.AdditionalToolbarAction
import at.shockbytes.core.ui.model.FabMenuOptions
import at.shockbytes.core.util.CoreUtils.toggleVisibility
import at.shockbytes.util.AppUtils
import com.leinardi.android.speeddial.SpeedDialActionItem
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import android.animation.ObjectAnimator
import android.animation.StateListAnimator


abstract class BottomNavigationBarActivity<T : ShockbytesInjector> : BaseActivity<T>() {

    protected var tabId: Int = 0

    var additionalToolbarActionItem: AdditionalToolbarAction? = null
        set(value) {
            field = value
            if (value != null) {
                setupAdditionalToolbarAction(value)
            }
        }

    abstract val options: BottomNavigationActivityOptions

    abstract val imageLoader: ImageLoader

    protected open val fragmentAnimations: Pair<Int, Int> = Pair(0, 0)

    abstract fun setupDarkMode()

    abstract fun createFragmentForIndex(index: Int): Fragment

    abstract fun showLoginScreen()

    abstract fun onFabMenuItemClicked(id: Int): Boolean

    abstract fun showWelcomeScreen(user: ShockbytesUser)

    abstract fun onBottomBarPageChanged(newPageIndex: Int)

    // TODO Fix this in the next iteration!
    abstract fun showMenuFragment()

    // ---------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(options.appTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        setSupportActionBar(activity_bottom_navigation_toolbar)

        tabId = savedInstanceState?.getInt("tabId") ?: options.defaultTab

        setupUI()
        initializeNavigation()
        setupDarkMode()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("tabId", tabId)
    }

    override fun onStart() {
        super.onStart()

        options.fabMenuOptions?.let { menuOptions ->
            setupFabMenu(menuOptions)
        }
    }

    private fun onPageSelected(position: Int) {
        onBottomBarPageChanged(position)

        activity_bottom_navigation_appBar.setExpanded(true, true)

        options.fabMenuOptions?.visiblePageIndices?.let { visiblePageIndices ->
            if (visiblePageIndices.contains(position)) {
                activity_bottom_navigation_mainFabMenu.toggleVisibility()
            } else {
                activity_bottom_navigation_mainFabMenu.hide()
            }
        }
    }

    // ---------------------------------------------------

    protected fun onUserEvent(event: LoginUserEvent) {
        when (event) {

            is LoginUserEvent.SuccessEvent -> {

                if (event.user != null) {
                    event.user.photoUrl?.let { photoUrl ->
                        imageLoader.loadImageUri(
                            this,
                            photoUrl,
                            activity_bottom_navigation_imgButtonMainToolbarMore,
                            circular = true,
                            withCrossFade = true
                        )
                    }
                    checkShowWelcomeScreen(event.user, event.showWelcomeScreen)
                } else {
                    activity_bottom_navigation_imgButtonMainToolbarMore.setImageResource(options.overflowIcon)
                }
            }

            is LoginUserEvent.LoginEvent -> {
                activity_bottom_navigation_imgButtonMainToolbarMore.setImageResource(options.overflowIcon)
                showLoginScreen()
            }

            is LoginUserEvent.ErrorEvent -> {
                showToast(getString(event.errorMsg))
            }
        }
    }

    protected fun enableDarkMode(isEnabled: Boolean) {
        val mode = if (isEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    protected fun castActionBarShadow(castShadow: Boolean) {

        val elevation = if (castShadow) {
            AppUtils.convertDpInPixel(4, this).toFloat()
        } else {
            0f
        }

        val stateListAnimator = StateListAnimator()
        stateListAnimator.addState(
            IntArray(0),
            ObjectAnimator.ofFloat(activity_bottom_navigation_appBar, "elevation", elevation)
        )
        activity_bottom_navigation_appBar.stateListAnimator = stateListAnimator
    }

    // ---------------------------------------------------

    private fun setupUI() {

        activity_bottom_navigation_imgButtonMainToolbarMore.setOnClickListener {
            showMenuFragment()
        }

        with(activity_bottom_navigation_txtMainToolbarTitle) {
            text = options.appName
            setTextColor(ContextCompat.getColor(this.context, options.titleColor))
        }

        activity_bottom_navigation_toolbar.background = ColorDrawable(
            ContextCompat.getColor(this, options.toolbarColor)
        )
        activity_bottom_navigation_mainBottomNavigation.background = ColorDrawable(
            ContextCompat.getColor(this, options.navigationBarColor)
        )
        activity_bottom_navigation_mainBottomNavigation.itemTextColor = ColorStateList.valueOf(
            ContextCompat.getColor(this, options.navigationItemTextColor)
        )
        activity_bottom_navigation_mainBottomNavigation.itemIconTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, options.navigationItemTintColor)
        )

        activity_bottom_navigation_imgButtonMainToolbarMore.setImageResource(options.overflowIcon)

        options.initialAdditionalToolbarAction?.let { action ->
            additionalToolbarActionItem = action
        }
    }

    private fun setupAdditionalToolbarAction(action: AdditionalToolbarAction) {

        activity_bottom_navigation_imgButtonMainToolbarAdditionalAction.apply {

            // Always set onClickListener
            setOnClickListener {
                action.onActionClick()
            }

            setOnLongClickListener {
                it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                showToast(action.title)
                true
            }

            if (action.changeWithAnimation) {
                animate()
                    .alpha(0f)
                    .scaleX(0.5f)
                    .scaleY(0.5f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        setImageResource(action.icon)
                        animate()
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setInterpolator(AccelerateDecelerateInterpolator())
                            .start()
                    }
                    .start()
            } else {
                setImageResource(action.icon)
                visibility = View.VISIBLE
                alpha = 1f
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupFabMenu(options: FabMenuOptions) {

        activity_bottom_navigation_mainFabMenu.visibility = View.VISIBLE

        val menu = MenuBuilder(this)
        menuInflater.inflate(options.menuId, menu)

        val bgColors = options.menuColorList
        menu.visibleItems.forEachIndexed { idx, item ->
            activity_bottom_navigation_mainFabMenu.addActionItem(
                SpeedDialActionItem.Builder(item.itemId, item.icon)
                    .setLabel(item.title.toString())
                    .setFabBackgroundColor(ContextCompat.getColor(this, bgColors[idx]))
                    .create()
            )
        }

        activity_bottom_navigation_mainFabMenu.setOnActionSelectedListener { item ->
            onFabMenuItemClicked(item.id)
        }

        activity_bottom_navigation_mainFabMenu.setMainFabClosedDrawable(
            ContextCompat.getDrawable(this, options.iconClosed)
        )
        activity_bottom_navigation_mainFabMenu.setMainFabOpenedDrawable(
            ContextCompat.getDrawable(this, options.iconOpened)
        )

    }

    private fun initializeNavigation() {

        activity_bottom_navigation_mainBottomNavigation.menu.apply {
            for (tab in options.tabs) {
                this.add(0, tab.id, 0, tab.title).also { item ->
                    item.icon = ContextCompat.getDrawable(this@BottomNavigationBarActivity, tab.icon)
                }
            }
        }

        activity_bottom_navigation_mainBottomNavigation.setOnNavigationItemSelectedListener { item ->
            colorNavigationItems(item)
            indexForNavigationItemId(item.itemId)?.let { index ->

                val fragment = createFragmentForIndex(index)
                showFragment(fragment)

                onPageSelected(index)
            }
            true
        }

        activity_bottom_navigation_mainBottomNavigation.selectedItemId = tabId
    }

    private fun showFragment(fragment: Fragment) {

        val (inAnim, outAnim) = fragmentAnimations

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(inAnim, outAnim, inAnim, outAnim)
            .replace(R.id.activity_bottom_navigation_content, fragment)
            .commit()
    }

    private fun colorNavigationItems(item: MenuItem) {
        val stateListRes: Int = options.tabs.find { it.id == item.itemId }?.stateListResource ?: 0
        val stateList = ContextCompat.getColorStateList(this, stateListRes)
        activity_bottom_navigation_mainBottomNavigation.itemIconTintList = stateList
        activity_bottom_navigation_mainBottomNavigation.itemTextColor = stateList
    }

    private fun checkShowWelcomeScreen(account: ShockbytesUser, showWelcomeScreen: Boolean) {
        if (showWelcomeScreen) {
            showWelcomeScreen(account)
        }
    }

    private fun indexForNavigationItemId(itemId: Int): Int? {
        val index = options.tabs.indexOfFirst { it.id == itemId }
        return if (index > -1) index else null
    }

}