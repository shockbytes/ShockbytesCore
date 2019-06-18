package at.shockbytes.core.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes

data class FabMenuOptions(
    @MenuRes val menuId: Int,
    val menuColorList: List<Int>,
    @DrawableRes val iconClosed: Int,
    @DrawableRes val iconOpened: Int,
    val visiblePageIndices: List<Int>
)