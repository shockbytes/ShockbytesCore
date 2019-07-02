package at.shockbytes.core.example


import android.net.Uri
import androidx.fragment.app.Fragment
import at.shockbytes.core.image.GlideImageLoader
import at.shockbytes.core.image.ImageLoader
import at.shockbytes.core.model.LoginUserEvent
import at.shockbytes.core.model.ShockbytesUser
import at.shockbytes.core.ui.activity.BottomNavigationBarActivity
import at.shockbytes.core.ui.model.AdditionalToolbarAction
import at.shockbytes.core.ui.model.BottomNavigationActivityOptions
import at.shockbytes.core.ui.model.BottomNavigationTab
import at.shockbytes.core.ui.model.FabMenuOptions

class MainActivity : BottomNavigationBarActivity<AppComponent>() {


    private val additionalToolbarActionItems = listOf(
        AdditionalToolbarAction(R.drawable.ic_fab_opened, R.string.action_1, true) { showToast("Closing time") },
        AdditionalToolbarAction(R.drawable.ic_search, R.string.action_2, true) { showToast("Search action") },
        AdditionalToolbarAction(R.drawable.ic_fab_closed, R.string.action_3, true) { showToast("Third action") }
    )

    private val fabMenuOptions = FabMenuOptions(
        menuId = R.menu.menu_fab,
        menuColorList = listOf(R.color.color_1, R.color.color_2),
        iconClosed = R.drawable.ic_fab_closed,
        iconOpened = R.drawable.ic_fab_opened,
        visiblePageIndices = listOf(0)
    )

    private val tabs by lazy {
        listOf(
            BottomNavigationTab(
                R.id.nav_item_1,
                R.drawable.navigation_item_current,
                R.drawable.ic_nav_drawable_1,
                "Wifi"
            ),
            BottomNavigationTab(
                R.id.nav_item_2,
                R.drawable.navigation_item_done,
                R.drawable.ic_nav_drawable_2,
                "Palette"
            ),
            BottomNavigationTab(
                R.id.nav_item_3,
                R.drawable.navigation_item_suggestions,
                R.drawable.ic_nav_drawable_3,
                "Radio"
            )
        )
    }
    override val options: BottomNavigationActivityOptions = BottomNavigationActivityOptions(
        tabs = tabs,
        defaultTab = R.id.nav_item_2,
        appName = "Demo App",
        viewPagerOffscreenLimit = 2,
        appTheme = R.style.AppTheme_NoActionBar,
        overflowIcon = R.drawable.ic_overflow,
        initialAdditionalToolbarAction = additionalToolbarActionItems[1],
        toolbarColor = R.color.toolbar_item_color,
        toolbarItemColor = R.color.toolbar_color,
        fabMenuOptions = fabMenuOptions,
        titleColor = R.color.colorPrimaryDark,
        navigationBarColor = R.color.navigation_bar_color,
        navigationItemTextColor = R.color.navigation_bar_item_text,
        navigationItemTintColor = R.color.navigation_bar_item_tint
    )

    override val fragmentAnimations: Pair<Int, Int> = Pair(R.anim.abc_fade_in, R.anim.abc_fade_out)

    override val imageLoader: ImageLoader
        get() = GlideImageLoader(R.drawable.ic_placeholder)

    override fun setupDarkMode() {
        showToast("Setup dark mode")
    }

    override fun createFragmentForIndex(index: Int): Fragment {
        return SimpleFragment.newInstance(tabs[index].title)
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

    override fun onBottomBarPageChanged(newPageIndex: Int) {
        additionalToolbarActionItem = additionalToolbarActionItems[newPageIndex]
    }

    override fun onResume() {
        super.onResume()
        castActionBarShadow(true)
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
