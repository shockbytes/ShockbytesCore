package at.shockbytes.core.ui.model

import android.support.annotation.*

data class BottomNavigationActivityOptions(
    val tabs: List<BottomNavigationTab>,
    @IdRes val defaultTab: Int,
    val appName: String,
    val viewPagerOffscreenLimit: Int,
    @StyleRes val appTheme: Int,
    @DrawableRes val overflowIcon: Int,
    val fabMenuOptions: FabMenuOptions?,
    val initialAdditionalToolbarAction: AdditionalToolbarAction?,
    @ColorRes val titleColor: Int,
    @ColorRes val toolbarColor: Int,
    @ColorRes val toolbarItemColor: Int,
    @ColorRes val navigationBarColor: Int,
    @ColorRes val navigationItemTintColor: Int,
    @ColorRes val navigationItemTextColor: Int
)