package at.shockbytes.core.example


import android.net.Uri
import android.support.v4.view.PagerAdapter
import at.shockbytes.core.image.GlideImageLoader
import at.shockbytes.core.image.ImageLoader
import at.shockbytes.core.model.LoginUserEvent
import at.shockbytes.core.model.ShockbytesUser
import at.shockbytes.core.ui.activity.BottomNavigationBarActivity
import at.shockbytes.core.ui.model.AdditionalToolbarAction
import at.shockbytes.core.ui.model.BottomNavigationActivityOptions
import at.shockbytes.core.ui.model.BottomNavigationTab

class MainActivity : BottomNavigationBarActivity<AppComponent>() {

    override val options: BottomNavigationActivityOptions = BottomNavigationActivityOptions(
        tabs = listOf(
            BottomNavigationTab(R.id.nav_item_1, R.drawable.navigation_item_current, R.drawable.ic_nav_drawable_1, "Wifi"),
            BottomNavigationTab(R.id.nav_item_2, R.drawable.navigation_item_done, R.drawable.ic_nav_drawable_2, "Palette"),
            BottomNavigationTab(R.id.nav_item_3, R.drawable.navigation_item_suggestions, R.drawable.ic_nav_drawable_3, "Radio")
        ),
        defaultTab = R.id.nav_item_2,
        appName = "Demo App",
        viewPagerOffscreenLimit = 2,
        appTheme = R.style.AppTheme_NoActionBar,
        fabMenuId = R.menu.menu_fab,
        fabMenuColorList = listOf(R.color.color_1, R.color.color_2),
        fabVisiblePageIndices = listOf(0),
        overflowIcon = R.drawable.ic_overflow,
        additionalToolbarAction = AdditionalToolbarAction(R.drawable.ic_search),
        toolbarColor = R.color.toolbar_color,
        toolbarItemColor = R.color.toolbar_item_color,
        fabClosedIcon = R.drawable.ic_fab_closed,
        fabOpenedIcon = R.drawable.ic_fab_opened,
        navigationBarColor = R.color.navigation_bar_color,
        navigationItemTextColor = R.color.navigation_bar_item_text,
        navigationItemTintColor = R.color.navigation_bar_item_tint
    )

    override val imageLoader: ImageLoader
        get() = GlideImageLoader(R.drawable.ic_placeholder)

    override fun onAdditionalToolbarActionClicked() {
        showToast("Start search activity")
    }

    override fun setupDarkMode() {
        showToast("Setup dark mode")
    }

    override fun setupPagerAdapter(tabs: List<BottomNavigationTab>): PagerAdapter {
        return SimplePagerAdapter(supportFragmentManager, tabs)
    }

    override fun showLoginScreen() {
        showToast("Show login fragment")
    }

    override fun onFabMenuItemClicked(id: Int): Boolean {
        showToast("on Fab menu item clicked $id")
        return false
    }

    override fun showWelcomeScreen(user: ShockbytesUser) {
        showToast("Show welcome screen")
    }

    override fun showMenuFragment() {
        showToast("Show menu fragment")
    }

    override fun bindViewModel() {
        val photoUri = Uri.parse("https://lh3.googleusercontent.com/a-/AAuE7mAmFn3EDDj2hotBqhphrJyF5pbuXTeUinLYDqbGOw")
        val shockbytesUser = ShockbytesUser("Martin Macheiner", "Martin", "mescht93@gmail.com", photoUri, null, null)
        onUserEvent(LoginUserEvent.SuccessEvent(shockbytesUser, false))
    }

    override fun unbindViewModel() {
    }

    override fun injectToGraph(appComponent: AppComponent?) {
        appComponent?.inject(this)
    }


}
