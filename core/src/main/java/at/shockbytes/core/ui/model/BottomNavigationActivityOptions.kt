package at.shockbytes.core.ui.model

import android.support.annotation.*

data class BottomNavigationActivityOptions(
    val tabs: List<BottomNavigationTab>,
    @IdRes val defaultTab: Int,
    val appName: String,
    val viewPagerOffscreenLimit: Int,
    @StyleRes val appTheme: Int,
    @MenuRes val fabMenuId: Int,
    val fabMenuColorList: List<Int>,
    val fabVisiblePageIndices: List<Int>,
    @DrawableRes val overflowIcon: Int,
    val additionalToolbarAction: AdditionalToolbarAction?,
    @ColorRes val toolbarColor: Int,
    @ColorRes val toolbarItemColor: Int,
    @DrawableRes val fabClosedIcon: Int,
    @DrawableRes val fabOpenedIcon: Int,
    @ColorRes val navigationBarColor: Int,
    @ColorRes val navigationItemTintColor: Int,
    @ColorRes val navigationItemTextColor: Int
)