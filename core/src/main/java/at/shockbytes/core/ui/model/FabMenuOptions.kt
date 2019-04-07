package at.shockbytes.core.ui.model

import android.support.annotation.DrawableRes
import android.support.annotation.MenuRes

data class FabMenuOptions(
    @MenuRes val menuId: Int,
    val menuColorList: List<Int>,
    @DrawableRes val iconClosed: Int,
    @DrawableRes val iconOpened: Int,
    val visiblePageIndices: List<Int>
)