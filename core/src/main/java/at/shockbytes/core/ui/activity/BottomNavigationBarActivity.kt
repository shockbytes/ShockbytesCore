package at.shockbytes.core.ui.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatDelegate
import android.support.v7.view.menu.MenuBuilder
import android.view.MenuItem
import android.view.View
import at.shockbytes.core.R
import at.shockbytes.core.ShockbytesInjector
import at.shockbytes.core.image.ImageLoader
import at.shockbytes.core.model.LoginUserEvent
import at.shockbytes.core.model.ShockbytesUser
import at.shockbytes.core.ui.model.BottomNavigationActivityOptions
import at.shockbytes.core.ui.activity.base.BaseActivity
import at.shockbytes.core.ui.model.BottomNavigationTab
import at.shockbytes.core.util.CoreUtils.toggleVisibility
import com.leinardi.android.speeddial.SpeedDialActionItem
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

abstract class BottomNavigationBarActivity<T : ShockbytesInjector> : BaseActivity<T>(),
    ViewPager.OnPageChangeListener {

    protected var tabId: Int = 0

    private lateinit var pagerAdapter: PagerAdapter

    abstract val options: BottomNavigationActivityOptions

    abstract val imageLoader: ImageLoader

    abstract fun onAdditionalToolbarActionClicked()

    abstract fun setupDarkMode()

    abstract fun setupPagerAdapter(tabs: List<BottomNavigationTab>): PagerAdapter

    abstract fun showLoginScreen()

    abstract fun onFabMenuItemClicked(id: Int): Boolean

    abstract fun showWelcomeScreen(user: ShockbytesUser)

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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("tabId", tabId)
    }

    override fun onStart() {
        super.onStart()
        setupFabMenu()
    }

    override fun onPageSelected(position: Int) {

        tabId = activity_bottom_navigation_mainBottomNavigation.menu.getItem(position).itemId
        activity_bottom_navigation_mainBottomNavigation.selectedItemId = tabId

        activity_bottom_navigation_appBar.setExpanded(true, true)
        if (options.fabVisiblePageIndices.contains(position)) {
            activity_bottom_navigation_mainFabMenu.toggleVisibility()
        } else {
            activity_bottom_navigation_mainFabMenu.hide()
        }
    }

    override fun onPageScrollStateChanged(state: Int) = Unit
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

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
                            circular = true)
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

    // ---------------------------------------------------

    private fun setupUI() {
        activity_bottom_navigation_imgButtonMainToolbarAdditionalAction.setOnClickListener {
            onAdditionalToolbarActionClicked()
        }
        activity_bottom_navigation_imgButtonMainToolbarMore.setOnClickListener {
            showMenuFragment()
        }

        activity_bottom_navigation_txtMainToolbarTitle.text = options.appName

        activity_bottom_navigation_toolbar.background = ColorDrawable(
            ContextCompat.getColor(this, options.toolbarColor)
        )
        activity_bottom_navigation_mainFabMenu.setMainFabClosedDrawable(
            ContextCompat.getDrawable(this, options.fabClosedIcon)
        )
        activity_bottom_navigation_mainFabMenu.setMainFabOpenedDrawable(
            ContextCompat.getDrawable(this, options.fabOpenedIcon)
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


        options.additionalToolbarAction?.let { action ->
            activity_bottom_navigation_imgButtonMainToolbarAdditionalAction.visibility = View.VISIBLE
            activity_bottom_navigation_imgButtonMainToolbarAdditionalAction.setImageResource(action.icon)
        }

        activity_bottom_navigation_txtMainToolbarTitle.setTextColor(
            ContextCompat.getColor(this, options.toolbarItemColor)
        )
    }

    @SuppressLint("RestrictedApi")
    private fun setupFabMenu() {

        val menu = MenuBuilder(this)
        menuInflater.inflate(options.fabMenuId, menu)

        val bgColors = options.fabMenuColorList
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
    }

    private fun initializeNavigation() {

        // Setup the ViewPager
        pagerAdapter = setupPagerAdapter(options.tabs)
        activity_bottom_navigation_viewPager.adapter = pagerAdapter
        activity_bottom_navigation_viewPager.removeOnPageChangeListener(this) // Remove first to avoid multiple listeners
        activity_bottom_navigation_viewPager.addOnPageChangeListener(this)
        activity_bottom_navigation_viewPager.offscreenPageLimit = options.viewPagerOffscreenLimit

        activity_bottom_navigation_mainBottomNavigation.menu.apply {
            for (tab in options.tabs) {
                this.add(0, tab.id, 0, tab.title).also { item ->
                    item.icon = ContextCompat.getDrawable(this@BottomNavigationBarActivity, tab.icon)
                }
            }
        }

        activity_bottom_navigation_mainBottomNavigation.setOnNavigationItemSelectedListener { item ->
            colorNavigationItems(item)
            indexForNavigationItemId(item.itemId)?.let { id ->
                activity_bottom_navigation_viewPager.currentItem = id
            }
            true
        }

        activity_bottom_navigation_mainBottomNavigation.selectedItemId = tabId
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